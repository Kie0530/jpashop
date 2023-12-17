package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional // javax, spring 2가지가 있는데 spring 어노테이션에 지원 메서드가 더 많음
// 클래스 레벨에 붙임으로써 내부 public 메서드들이 전부 걸려들어 레이지로딩 같은 데이터 트랜잭션 지원을 받을 수 있음
// 디폴트: readonly false
public class MemberService {

    private final MemberRepository memberRepository;
    // final 쓰는 이유: (1)변경할 일이 없음 (2) 생성자에서 누락한 경우 컴파일 시점에 체크를 해줌

    @Autowired // 없어도 스프링이 자동으로 주입해줘서 어노테이션 생략 가능
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }
    // 이 생성자를 생략하려면 클래스에 @AllArgsConstructor 또는 @RequiredArgsConstructor 추가
    // @Required~ 어노테이션은 final 붙은 필드로만 생성자를 만들기 때문에 제일 적절함

//    회원 가입
    public Long join(Member member) {
      validateDuplicateMember(member);
      memberRepository.save(member);
      return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        //EXCEPTION
        List<Member> findMembers = memberRepository.findByName(member.getName());
        if(!findMembers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    // 회원 전체 조회
    @Transactional(readOnly = true) // 읽기 전용으로 드라이버따라 DB 리소스를 적게 쓰게 하는 성능 최적화용 어노테이션
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }
    @Transactional(readOnly = true) // 데이터 트랜잭션을 못하게 되기 때문에 읽기 메서드에만 넣어야 함
    public Member findOne(Long id){
        return memberRepository.findOne(id);
    }

}
