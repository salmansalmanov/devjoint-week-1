package com.salman.week1.scheduler;

import com.salman.week1.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class MemberScheduler {
    private final MemberRepository memberRepository;

    @Transactional
    @Scheduled(cron = "0 0 0 * * *")
    public void deleteBlockedMembers() {
        LocalDateTime threshold = LocalDateTime.now().minusDays(30);
        memberRepository.deleteBlockedMembersBefore(threshold);
    }
}
