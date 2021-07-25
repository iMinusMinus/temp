package ${package};

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.dbunit.Assertion;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.Test;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * load expected data from xml/csv/xls(x) etc.
 * compare with db data, then make an Assertion.
 * You can backup db data with IDataSet.write().
 *
 * @author iMinusMinus
 * @date 2019-06-23
 */
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(classes = {AppConfig.class, DalConfig.class})
public abstract class BaseDbUnitTest extends ContainerBase { // FIXME 使用不同的上下文会导致h2初始脚本重复执行而报错

    @Resource
    private DataSource dataSource;

    private IDatabaseConnection conn;

    @Before
    public void setUp() {
        try {
            conn = new DatabaseConnection(DataSourceUtils.getConnection(dataSource));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected IDatabaseConnection getConnection() {
        return conn;
    }

    protected IDataSet getExpectedDataSet() throws Exception {
        return null;//new FlatXmlDataSet();new CsvDataSet();
    }

    @Test
    public void testDataSet() throws Exception {
        IDataSet expected = getExpectedDataSet();
        IDataSet actual = getActualDataSet();
        Assertion.assertEquals(expected, actual);
    }

    protected IDataSet getActualDataSet() throws Exception {
        return null;//conn.createDataSet();
    }

    protected ITable getExpectedTable() throws Exception {
        return null;//expectedDataSet.getTable();
    }

    @Test
    public void testTable() throws Exception {
        ITable expected = getExpectedTable();
        ITable actual = getActualTable();
        Assertion.assertEquals(expected, actual);
    }

    protected ITable getActualTable() throws Exception {
        return null;//actualDataSet.getTable();
    }

    @After
    public void tearDown() throws Exception {
        if(conn != null) {
            conn.close();
        }
    }
}