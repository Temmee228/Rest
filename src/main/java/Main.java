import org.example.dataBaseConfig.ConnectionPool;
import org.example.util.InitSqlScheme;

public class Main {
    public static void main(String[] args) {
        ConnectionPool connectionPool = ConnectionPool.getInstance();
        InitSqlScheme.initSqlScheme(connectionPool);
        InitSqlScheme.initSqlData(connectionPool);
    }
}
