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
            /*
            String query = "select m from Member m ";

            List<Member> resultList = em.createQuery(query, Member.class)
                .getResultList();

            for (Member member : resultList) {
                System.out.println("member = " + member.getUsername() + " , " + member.getTeam().getName());

                // N + 1 문제 발생 예제
                
            } */


            /*

            String query = "select m from Member m join fetch m.team";

            List<Member> resultList = em.createQuery(query, Member.class)
                .getResultList();

            for (Member member : resultList) {
                System.out.println("member = " + member.getUsername() + " , " + member.getTeam().getName());
                //N + 1 문제 발생 해결

            } */

            //컬렉션 fetch join

            String query = "select distinct t from Team t join fetch t.members";

            List<Team> resultList = em.createQuery(query, Team.class)
                .getResultList();

            for (Team team : resultList) {
                System.out.println(" team = " + team.getName() + " | " + team.getMembers().size());

                for (Member member : team.getMembers()){
                    System.out.println(" -> member = " + member);

                }

            }

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