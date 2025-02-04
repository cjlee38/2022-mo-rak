package com.morak.back.poll.application;

import com.morak.back.auth.domain.Member;
import com.morak.back.auth.domain.MemberRepository;
import com.morak.back.auth.domain.Team;
import com.morak.back.auth.domain.TeamRepository;
import com.morak.back.poll.domain.Poll;
import com.morak.back.poll.domain.PollItem;
import com.morak.back.poll.domain.PollItemRepository;
import com.morak.back.poll.domain.PollRepository;
import com.morak.back.poll.domain.PollStatus;
import com.morak.back.poll.exception.ResourceNotFoundException;
import com.morak.back.poll.ui.dto.PollCreateRequest;
import com.morak.back.poll.ui.dto.PollItemRequest;
import com.morak.back.poll.ui.dto.PollItemResponse;
import com.morak.back.poll.ui.dto.PollItemResultResponse;
import com.morak.back.poll.ui.dto.PollResponse;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional
public class PollService {

    private final PollRepository pollRepository;
    private final MemberRepository memberRepository;
    private final TeamRepository teamRepository;
    private final PollItemRepository pollItemRepository;

    private String tempCode = "ABCD";

    public Long createPoll(Long teamId, Long memberId, PollCreateRequest request) {
        Member member = memberRepository.findById(memberId).orElseThrow(ResourceNotFoundException::new);
        Team team = teamRepository.findById(teamId).orElseThrow(ResourceNotFoundException::new);

        Poll savedPoll = pollRepository.save(request.toPoll(member, team, PollStatus.OPEN, tempCode));
        List<PollItem> items = request.toPollItems(savedPoll);
        pollItemRepository.saveAll(items);

        return savedPoll.getId();
    }

    public List<PollResponse> findPolls(Long teamId, Long memberId) {
        Member member = memberRepository.getById(memberId);
        List<Poll> polls = pollRepository.findAllByTeamId(teamId);

        return polls.stream()
                .map(poll -> PollResponse.from(poll, member))
                .collect(Collectors.toList());
    }

    public void doPoll(Long tempMemberId, Long pollId, PollItemRequest pollItemRequest) {
        Member member = memberRepository.findById(tempMemberId).orElseThrow(ResourceNotFoundException::new);
        Poll poll = pollRepository.findById(pollId).orElseThrow(ResourceNotFoundException::new);
        List<PollItem> items = pollItemRepository.findAllById(pollItemRequest.getItemIds());
        poll.doPoll(items, member);
    }

    public PollResponse findPoll(Long teamId, Long memberId, Long pollId) {
        Member member = memberRepository.findById(memberId).orElseThrow(ResourceNotFoundException::new);

        Poll poll = pollRepository.findByIdAndTeamId(pollId, teamId).orElseThrow(ResourceNotFoundException::new);

        return PollResponse.from(poll, member);
    }

    public List<PollItemResponse> findPollItems(Long teamId, Long pollId) {
        Poll poll = pollRepository.findByIdAndTeamId(pollId, teamId).orElseThrow(ResourceNotFoundException::new);
        return poll.getPollItems()
                .stream()
                .map(PollItemResponse::from)
                .collect(Collectors.toList());
    }

    public List<PollItemResultResponse> findPollItemResults(Long teamId, Long pollId) {
        Poll poll = pollRepository.findByIdAndTeamId(pollId, teamId).orElseThrow(ResourceNotFoundException::new);
        return poll.getPollItems()
                .stream()
                .map(PollItemResultResponse::of)
                .collect(Collectors.toList());
    }

    public void deletePoll(Long teamId, Long memberId, Long id) {
        Poll poll = pollRepository.findByIdAndTeamId(id, teamId).orElseThrow(ResourceNotFoundException::new);
        Member member = memberRepository.findById(memberId).orElseThrow(ResourceNotFoundException::new);

        poll.validateHost(member);

        pollRepository.deleteById(id);
    }

    public void closePoll(Long teamId, Long memberId, Long id) {
        Poll poll = pollRepository.findByIdAndTeamId(id, teamId).orElseThrow(ResourceNotFoundException::new);
        Member member = memberRepository.findById(memberId).orElseThrow(ResourceNotFoundException::new);

        poll.close(member);
    }
}
