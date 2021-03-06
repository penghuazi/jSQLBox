
package test.coverage_test;

import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.github.drinkjava2.jsqlbox.Dao;

import test.config.PrepareTestContext;
import test.config.po.User;

public class SqlBoxContextTest {
	@Before
	public void setup() {
		System.out.println("=============================Testing SqlBoxTest=============================");
		PrepareTestContext.prepareDatasource_setDefaultSqlBoxConetxt_recreateTables();
	}

	@After
	public void cleanUp() {
		PrepareTestContext.closeDatasource_closeDefaultSqlBoxConetxt();
	}

	/**
	 * Pagination test, already tested on H2, MySql, MSSQL, Oracle
	 */
	@Test
	public void paginationTest() {
		for (int i = 1; i <= 80; i++) {
			User u = new User();
			u.setUserName("zhang san");
			u.setAge(i);
			u.insert();
		}
		User u = new User();
		Dao.getDefaultContext().setShowSql(true);
		List<Map<String, Object>> list = Dao.queryForList("select", Dao.pagination(2, 10), " * from ", u.table(),
				Dao.orderBy(u.AGE()));

		for (Map<String, Object> map : list)
			System.out.print(map.get(u.AGE()) + ",");
		System.out.println();
		Assert.assertEquals(11, (int) list.get(0).get(u.AGE()));
	}

}
