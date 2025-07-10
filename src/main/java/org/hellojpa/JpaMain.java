package org.hellojpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import org.hellojpa.jpql.Address;
import org.hellojpa.jpql.Member;
import org.hellojpa.jpql.MemberDTO;
import org.hellojpa.jpql.Team;

import java.util.List;

public class JpaMain {
    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {

            Team teamA = new Team();
            teamA.setName("팀A");
            em.persist(teamA);

            Team teamB = new Team();
            teamB.setName("팀B");
            em.persist(teamB);

            Member member1 = new Member();
            member1.setUsername("회원1");
            member1.setTeam(teamA);
            em.persist(member1);

            Member member2 = new Member();
            member2.setUsername("회원2");
            member2.setTeam(teamB);
            em.persist(member2);

            Member member3 = new Member();
            member3.setUsername("회원3");
            member3.setTeam(teamB);
            em.persist(member3);


            em.flush();
            em.clear();

            // ✅ JPQL에서 엔티티 객체 자체를 비교하는 방식
            //    내부적으로는 해당 엔티티의 기본 키(PK)를 기준으로 SQL로 변환됨

            /*
            String query = "select  m from Member m where m =:member";

            List<Member> resultList = em.createQuery(query,Member.class)
                .setParameter("member",member1)
                .getResultList();


             */

            // ✅ 위 JPQL은 아래 JPQL과 동일한 SQL로 변환됨
            //    즉, 엔티티 객체 전체 비교는 결국 해당 객체의 ID(PK)로 비교하는 것과 같음

            String query = "select  m from Member m where m.id =:memberId";

            List<Member> resultList = em.createQuery(query,Member.class)
                .setParameter("memberId",member1.getId())
                .getResultList();

            for (Member member : resultList) {
                System.out.println("member = " + member);

            }

            // 🔄 결국 두 쿼리는 동일한 SQL로 변환됨:
            // select * from member where id = ?

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
        emf.close();

    }
}