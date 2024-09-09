package section02.crud;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class EntityManagerCrudTest {

    private EntityManagerCrud entityManagerCrud;

    @BeforeEach
    void init(){this.entityManagerCrud = new EntityManagerCrud(); }

    @CsvSource({"1,1", "2,2", "3,3"})
    @DisplayName("메뉴 코드로 메뉴 조회")
    @ParameterizedTest
    void testFindMethodByMenuCode(int menuCode, int expected){

        // when
        Menu foundMenu = entityManagerCrud.findMenuByMenuCode(menuCode);

        // then
        assertEquals(expected, foundMenu.getMenuCode());
    }

    @DisplayName("새로운 메뉴 추가 테스트")
    @ParameterizedTest
    @MethodSource("newMenu")
    void testRegist(String menuName, int menuPrice, int categoryCode, String orderableStatus){
        // when
//        Menu menu = new Menu(menuName, menuPrice, categoryCode, orderableStatus);
//        Long count = entityManagerCrud.(menu);
//
//        // then
//        assertEquals(24, count);

        // given
    }

    @DisplayName("메뉴 이름 수정 테스트")
    @ParameterizedTest
    @CsvSource("1. 변경 된 이름")
    void testModifyMenuName(int menuCode, String menuName){
        // when
        Menu modifiedMenu = entityManagerCrud.modifyMenuName(menuCode, menuName);

        // then
        assertEquals(menuName, modifiedMenu.getMenuName());
    }

    @DisplayName("메뉴 삭제 테스트")
    @ParameterizedTest
    @ValueSource(ints = {107})
    void testRemoveMenu(int menuCode){
        // when
        Long count = entityManagerCrud.removeAndeReturnAllCount(menuCode);

        // then
        assertEquals(2L, count);
    }
}