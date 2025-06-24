package org.hellojpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import org.hellojpa.jpql.Address;
import org.hellojpa.jpql.Member;
import org.hellojpa.jpql.MemberDTO;

import java.util.List;

public class JpaMain {
    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try{
            Member member = new Member();
            member.setUsername("member1");
            member.setAge(10);
            em.persist(member);

            em.flush();
            em.clear();
            

            /* 프로젝션 - 여려 값 조회 1
            List resultList = em.createQuery("select distinct m.username,m.age from Member m")
                .getResultList();

            Object o = resultList.get(0);
            Object[] result = (Object[]) o;
            System.out.println("username = " + result[0]);
            System.out.println("age = " + result[1]); */

            /* 프로젝션 - 여려 값 조회 2
            List<Object[]> resultList = em.createQuery("select distinct m.username,m.age from Member m")
                .getResultList();

            Object[] result = resultList.get(0);
            System.out.println("username = " + result[0]);
            System.out.println("age = " + result[1]); */


            /* 프로젝션 여러값 조회 - new 명령어로 조회 */

            List<MemberDTO> result = em.createQuery("select new org.hellojpa.jpql.MemberDTO( m.username , m.age ) from Member m", MemberDTO.class)
                .getResultList();

            MemberDTO memberDTO = result.get(0);
            System.out.println("memberDTO = " + memberDTO.getUsername());
            System.out.println("memberDTO = " + memberDTO.getAge());


            tx.commit();
        } catch (Exception e){
            tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
        emf.close();

    }
}