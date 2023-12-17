package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions.*;
//import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

//@RunWith(SpringRunner.class) -> Junit4에서 테스트 작성 시 필요한 어노테이션이었으나 Junit5에서는 생략 가능
@SpringBootTest // 실제 application을 띄워 DB와 커넥션을 만들어 라이브 테스트가 가능하게끔 해줌
@Transactional // 데이터 트랜젝션을 커밋하지 않고 롤백하는 역할
public class MemberServiceTest {
    /**
     *
     * 데이터가 DB에 가는 것을 봐야 하기 때문에
     * 메모리 모드로 테스트 작성하는 것이 중요하다.
     */

   @Autowired MemberService memberService;
   @Autowired MemberRepository memberRepository;

    @Test
    public void 회원가입() throws Exception {
        //given
        Member member = new Member();
        member.setName("kim");

        //when
        /**
         * entityManager persist != DB INSERT
         * 따라서 테스트시 INSERT 쿼리를 쓰지 않는다.
         * (INSERT 되는 것은 데이터 트랜젝션이 커밋되면서, entityManager flush 되는 시점)
         * 테스트 케이스에 @Rollback(false) 애너테이션을 붙이면 INSERT문이 생성된다. (@Transitional이 있으면 롤백되긴 함)
         */
        Long savedId = memberService.join(member);

        //then
        assertEquals(member, memberRepository.findOne(savedId));
    }

    @Test
    public void 중복회원예외처리() throws Exception {
        //given
        Member member1 = new Member();
        member1.setName("kim");
        Member member2 = new Member();
        member2.setName("kim");

        //when
        memberService.join(member1);
        /**
         * Junit5에서는 @Test의 expected 속성 사용 X
         * 대신 assertThrows() 메서드를 사용하여 예외 발생 여부 검증
         */
        assertThrows(IllegalStateException.class, () -> memberService.join(member2));

        //then
    }

}