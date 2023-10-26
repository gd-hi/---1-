package JDBCTest;

// 010-xxxx 출력 해결

/*
//기초작업
1. 데이터 베이스 드라이버 연결 ( DriverManager.getConnection("jdbc:mysql://DB로컬호스트/스키마"
                ,"ID","PW");)
2. 데이터 베이스와 자바 연결 ( Connection)
3. 쿼리문 실행 ( Statement )
4. 결과값 반영 ( ResultSet )

//전화번호부 프로그램
1. 입력 , 데이터 입력, 데어터가 중복이면 전화번호부에 중복입니다. 다른 방식의 이름으로 입력하세요.
2. 검색 , 데이터가 없으면 전화번호부에 없습니다. 출력
3. 삭제 , 데이터가 없으면 전화번호부에 없습니다. 출력
4. 출력 , 데이터 전체 출력
5. 종료 , 프로그램 종료
 */

import javax.xml.crypto.Data;
import java.sql.*;
import java.util.Scanner;

//데이터
class Phone_Data {
    String name, phoneNumber, address;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}

class SQLC {

    //2. 데이터 베이스와 자바 연결 ( Connection )
    private static Connection con;

    //1. DB 드라이버 연결
    SQLC() throws SQLException {
        con = DriverManager.getConnection("jdbc:mysql://localhost:3306/phone", "root", "1234");
    }

    //3. 쿼리문 실행 ( Statement )
    private static PreparedStatement stm;
    //4. 결과값 반영 ( ResultSet )
    ResultSet rs;
    // JDBC 기초 작업 완료


    // < 전화번호부 프로그램>
    //1. 데이터 입력
    void dataInsert(Phone_Data phone_data) {
        // 데이터를 쿼리문으로 넣기
        try {
            //실행 할 쿼리문을 prepareStatement 로 설정
            stm = con.prepareStatement("INSERT INTO phone VALUE(?, ?, ?);"); // name, phoneNumber, address
            Scanner sc = new Scanner(System.in);

            //데이터 set
            System.out.print("이름 입력 : ");
            phone_data.setName(sc.nextLine());
            System.out.print("전화 번호 입력 : ");
            phone_data.setPhoneNumber(sc.nextLine());
            System.out.print("주소 입력 : ");
            phone_data.setAddress(sc.nextLine());
            //n번 째 인덱스 ?에 값 get
            stm.setString(1, phone_data.getName()); //이름
            stm.setString(2, phone_data.getPhoneNumber()); //전화번호
            stm.setString(3, phone_data.getAddress()); //주소

            //executeUpdate -> DDL 실행, 없으면 실행 결과 반영 안함
            int num = stm.executeUpdate();
            //num 라는 변수에 SQL문의 영향을 받는 행 수를 반환 받아 메세지 출력
            if (num != 0) {
                System.out.println("★★★★★데이터 등록 완료★★★★★");
            }
        }

        // SQLIntegrityConstraintViolationException >> Primary key 예외 체크 (구글링 함)
        catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("★★★★★이미 등록된 이름입니다.★★★★★");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    //2. 데이터 검색 ( name ), 있으면 출력, 없으면 전화번호부에 없습니다. 출력
    void searchInformation(String name) throws SQLException {
        //실행할 쿼리문
        stm = con.prepareStatement("select name, rpad(substr(phoneNumber, 1, 4), 8, 'x') as phoneNumber, address FROM phone where name = ?;");
        //name이 null이면 rs.next() -> false
        stm.setString(1, name);
        // rs의 자료형은 ResultSet
        rs = stm.executeQuery();

        // rs.next()를 이용해서 true면 ResultSet 커서 위치의 처리 행이 있는 경우의 반환값 / 존재 하지 않으면 false
        // rs.next
        if(rs.next()) {
            while (true) {
                System.out.println("======================================");
                System.out.print("이름 : ");
                System.out.print(rs.getString("name")+ " / ");
                System.out.print("전화 번호 : ");
                System.out.print(rs.getString("phoneNumber")+ " / ");
                System.out.print("주소 : ");
                System.out.println(rs.getString("address"));
                System.out.println("======================================");
                break;
            }
        }
        else{
            System.out.println("★★★★★전화번호부에 없습니다.★★★★★");
        }

    }

    //3. 데이터 삭제 ( name ), 있으면 출력, 없으면 전화번호부에 없습니다. 출력
    void deleteInformation(String name) throws SQLException {
        //실행할 쿼리문
        stm = con.prepareStatement("delete from phone where name = ?;");
        stm.setString(1, name);

        //executeUpdate -> 데이터가 잘 들어갔으면 1, 데이터가 들어가지 않았으면 0
        int result = stm.executeUpdate();
        if (result == 0) {
            System.out.println("★★★★★전화번호부에 없습니다.★★★★★");
        } else {
            System.out.println("★★★★★데이터 삭제 완료★★★★★");
        }
    }

    // 2, 3의 데이터 검색, 삭제를 위한 이름 검색 메소드
    String findInformation() {
        Scanner sc = new Scanner(System.in);
        System.out.print("이름 : ");
        return sc.next();
    }

    //4. 데이터 전체 출력
    void selectAll() throws SQLException {
        //실행할 쿼리문
        stm = con.prepareStatement("select name, rpad(substr(phoneNumber, 1, 4), 8, 'x') as phoneNumber, address FROM phone;");
        //쿼리문 반영
        ResultSet rs = stm.executeQuery();

        //전체 행 출력. ResultSet 커서를 다음 행으로 이동 하는 메소드
        //true면 ResultSet 커서 위치의 처리 행이 있는 경우의 반환값
        //false면 ResultSet 커서 위치의 처리 행이 없는 경우의 반환값 - 종료, EOF(End Of File)
        while (rs.next()) {
            System.out.println("======================================");
            System.out.print("이름 : ");
            System.out.print(rs.getString("name")+ " / ");
            System.out.print("전화 번호 : ");
            System.out.print(rs.getString("phoneNumber")+ " / ");
            System.out.print("주소 : ");
            System.out.println(rs.getString("address"));
            System.out.println("======================================");

        }
    }
}


public class Phone {
    public static void main(String[] args) throws SQLException {
        Scanner sc = new Scanner(System.in);

        Phone_Data phone_data = new Phone_Data();

        SQLC sq = new SQLC();

        while (true) {
            System.out.println("<전화번호부 프로그램>");
            System.out.print("1. 입력 2. 검색 3. 삭제 4. 출력 5. 종료 : ");
            int num = sc.nextInt();

            //1. 입력
            if (num == 1) {
                sq.dataInsert(phone_data);
                sq.selectAll();
            }
            //2. 검색
            else if (num == 2) {
                sq.searchInformation(sq.findInformation());
            }
            //3. 삭제
            else if (num == 3) {
                sq.deleteInformation(sq.findInformation());
                System.out.println("--------현재 데이터--------");
                sq.selectAll();
                System.out.println("-------------------------");
            }
            //4. 출력
            else if (num == 4) {
                System.out.println("--------현재 데이터--------");
                sq.selectAll();
                System.out.println("-------------------------");
            }
            //5. 종료
            else {
                System.out.println("프로그램을 종료합니다.");
                break;
            }
        }
    }

}