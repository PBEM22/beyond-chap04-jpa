package section02.crud;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import section01.entitymanager.EntityManagerGenerator;

public class EntityManagerCrud {

//    private EntityManagerCRUD;
    private EntityManager entityManager;

    /* 1. 특정 메뉴 코드로 메뉴를 조회하는 기능 */
    public Menu findMenuByMenuCode(int menuCode){
        entityManager = EntityManagerGenerator.getInstance();

        return entityManager.find(Menu.class, menuCode);
    }

//    public Long saveAndRegurnAllCount(Menu menu) {
//        EntityTransaction entityTransaction = entityManager.getTransaction();
//        entityTransaction.commit();
////        return entityManager.get
//    }

    /* 3. 메뉴 이름 수정 기능 */
    public Menu modifyMenuName(int menuCode, String menuName){
        entityManager = EntityManagerGenerator.getInstance();
        Menu foundMenu = entityManager.find(Menu.class, menuCode);

        EntityTransaction entityTransaction = entityManager.getTransaction();
        entityTransaction.commit();

        foundMenu.setMenuName(menuName);

        entityTransaction.commit();

        return foundMenu;
    }

    /* 4. 특정 메뉴 코드로 메뉴 삭제하는 기능 */
    public Long removeAndeReturnAllCount(int menuCode){
        entityManager = EntityManagerGenerator.getInstance();
        Menu foundMenu = entityManager.find(Menu.class, menuCode);

        EntityTransaction entityTransaction = entityManager.getTransaction();
        entityTransaction.begin();

        entityManager.remove(foundMenu);

        entityTransaction.commit();

        return 2L;
    }
}
