package section01.entitymanager;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class EntityManagerGeneratorTest {

    @Test
    @DisplayName("엔터티 매니저 팩토리 생성 확인")
    void testGeneratorEntityManagerFactory(){
        // given
        // when
        EntityManagerFactory factory = EntityManagerFactoryGenerator.getInstance();

        // then
        assertNotNull(factory);

    }

    @Test
    @DisplayName("엔터티 매니저 팩토리 싱글톤 인스턴스 확인")
    void testIsEntityManagerFactorySingletonInstance(){
        // given
        // when

        // then
//        assertEquals(factory)
    }

    @Test
    @DisplayName("엔터티 매니저 생성 확인")
    void testGenerateEntityManager(){
        // given
        // when
        EntityManager entityManager = EntityManagerGenerator.getInstance();

        // then
        assertNotNull(entityManager);
    }

    @Test
    @DisplayName("엔터티 매니저 스코프 확인")
    void testEntityManagerLifeCycle(){
        // given
        // when
        EntityManager entityManager1 = EntityManagerGenerator.getInstance();
        EntityManager entityManager2 = EntityManagerGenerator.getInstance();

        // then
        assertNotEquals(entityManager1, entityManager2);
        assertNotEquals(entityManager1.hashCode(), entityManager2.hashCode());
    }
}
