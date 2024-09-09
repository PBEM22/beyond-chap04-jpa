package section03.entity;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class EntityLifeCycleTest {

    private EntityLifeCycle entityLifeCycle;

    @BeforeEach
    void init(){this.entityLifeCycle = new EntityLifeCycle();}

    @DisplayName("비영속 테스트")
    @ParameterizedTest
    @ValueSource(ints = {1, 2})
    void testTransient(int menuCode){
        // when
        Menu foundMenu = entityLifeCycle.findMenuByMenuCode(menuCode);

        Menu newMenu = new Menu(

                foundMenu.getMenuCode(),
                foundMenu.getMenuName(),
                foundMenu.getMenuPrice(),
                foundMenu.getCategoryCode(),
                foundMenu.getOrderableStatus()

        );

        EntityManager entityManager = entityLifeCycle.getEntityManager();

        // then
        assertTrue(entityManager.contains(foundMenu));  // foundMenu 영속성 컨텍스트에서 관리 되는 영속 상태의 객체
        assertFalse(entityManager.contains(newMenu));   // newMenu 영속성 컨텍스트에서 관리 되지 않는 비영속 상태의 객체
        
    }

    @DisplayName("다른 엔터티 매니저가 관리하는 엔터티의 영속성 테스트")
    @ParameterizedTest
    @ValueSource(ints = {1, 2})
    void testManagerOtherEntityManager(int menuCode){
        // when
//        EntityManager entityManager = EntityManagerGenerator.getInstance();
        Menu menu1 = entityLifeCycle.findMenuByMenuCode(menuCode);
        Menu menu2 = entityLifeCycle.findMenuByMenuCode(menuCode);

        // then
        assertNotEquals(menu1, menu2);
    }

    @DisplayName("같은 엔터티 매니저가 관리하는 엔터티의 영속성 테스트")
    @ParameterizedTest
    @ValueSource(ints = {1, 2})
    void testManagerOtherEntityManage(int menuCode){
        // when
        EntityManager entityManager = EntityManagerGenerator.getInstance();
        Menu menu1 = entityLifeCycle.findMenuByMenuCode(menuCode);
        Menu menu2 = entityLifeCycle.findMenuByMenuCode(menuCode);

        // then
        assertNotEquals(menu1, menu2);
    }

    @DisplayName("준영속화 detach 테스트")
    @ParameterizedTest
    @CsvSource({"11, 1000", "12, 1000"})
    void testDetachEntity(int menuCode, int menuPrice){
        // given
        EntityManager entityManager = EntityManagerGenerator.getInstance();
        EntityTransaction entityTransaction = entityManager.getTransaction();

        // when
        entityTransaction.begin();
        Menu foundMenu = entityManager.find(Menu.class, menuCode);
        // detach : 특정 엔터티만 준영속 상태(영속성 컨텍스트가 관리하던 객체를 관리하지 않는 상태)로 만든다.
        entityManager.detach(foundMenu);
        foundMenu.setMenuPrice(menuPrice);

        entityManager.flush();

        entityTransaction.rollback();
    }

    @DisplayName("detach 후 merge한 데이터 save 테스트")
    @Test
    void testMergeSave(){
        // given
        EntityManager entityManager = EntityManagerGenerator.getInstance();
        EntityTransaction entityTransaction = entityManager.getTransaction();
        Menu foundMenu = entityManager.find(Menu.class, 20);
        entityManager.detach(foundMenu);

        // when
        entityTransaction.begin();
        foundMenu.setMenuName("치약맛 초코 아이스크림");
        foundMenu.setMenuCode(999);

        entityManager.merge(foundMenu);
        entityTransaction.commit();

        // then
        assertEquals("치약맛 초코 아이스크림", entityManager.find(Menu.class, 999).getMenuName());

    }

    @DisplayName("준영속화 clear 테스트")
    @ParameterizedTest
    @ValueSource(ints = {1, 3})
    void testClearPersistenceContext(int menuCode){
        // given
        EntityManager entityManager = EntityManagerGenerator.getInstance();
        Menu foundMenu = entityManager.find(Menu.class, menuCode);

        // when
        // clear :
        entityManager.clear();

        // then
        Menu expectedMenu = entityManager.find(Menu.class, menuCode);
    }

    @DisplayName("준영속화 close 테스트")
    @ParameterizedTest
    @ValueSource(ints = {1, 3})
    void testClosePersistenceContext(int menuCode){
        // given
        EntityManager entityManager = EntityManagerGenerator.getInstance();
        Menu foundMenu = entityManager.find(Menu.class, menuCode);

        // when
        // close : 영속성 컨텍스트 종료된다 -> 영속성 컨텍스트 내의 모든 엔터티는 준영속화 된다.
        entityManager.close();

        // then
        assertThrows(
                IllegalStateException.class,
                () -> entityManager.find(Menu.class, menuCode)
        );
    }

    @DisplayName("영속성 엔터티 삭제 remove 테스트")
    @ParameterizedTest
    @ValueSource(ints = {2})
    void testRemoveEntity(int menuCode){

        // given
        EntityManager entityManager = EntityManagerGenerator.getInstance();
        EntityTransaction entityTransaction = entityManager.getTransaction();
        Menu foundMenu = entityManager.find(Menu.class, menuCode);

        // when
        entityTransaction.begin();

        // remove : 엔터티를 영속성 컨텍스트 및 데이터 베이스에서 삭제한다.
        // 단, 트랜잭션을 제어하지 않으면 데이터 베이스에 영구 반영 되지는 않는다.
        entityManager.remove(foundMenu);

        // then
        Menu refoundMenu = entityManager.find(Menu.class, menuCode);
        assertNull(refoundMenu);
        entityTransaction.rollback();
    }
}