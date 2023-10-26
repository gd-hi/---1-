package JDBCTest;

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

    //쿼리문으로 데이터 넣기
    void dataInsert(Phone_Data phone_data) {
        // 데이터를 쿼리문으로 넣기
        try {
            //실행 할 쿼리문을 prepareStatement 로 설정
            stm = con.prepareStatement("INSERT INTO 전화번호부 VALUE(?, ?, ?);"); // name, phoneNumber, address
            //n번 째 인덱스 ?에 값 넣기
            stm.setString(1, phone_data.getName()); //이름
            stm.setString(2, phone_data.getPhoneNumber()); //전화번호
            stm.setString(3, phone_data.getAddress()); //주소

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    // < 전화번호부 프로그램>

    //1. 데이터 입력 -> DataInputClass

    //2. 데이터 검색 ( name ), 있으면 출력, 없으면 전화번호부에 없습니다. 출력
    void searchInformation(String name) throws SQLException {
        //실행할 쿼리문
        stm = con.prepareStatement("select * from 전화번호부 where name = ?;");
        //set으로 객체를 만들고 name 데이터 연결
        stm.setString(1, name);

        // rs의 자료형은 ResultSet 이다.
        rs = stm.executeQuery();
        // rs.next()를 이용해서 검색한 결과가 존재 하면, true / 존재하지 않으면 false
        if(rs.next() == false){
            System.out.println("전화번호부에 없습니다.");
        }

    }

    //3. 데이터 삭제 ( name ), 있으면 출력, 없으면 전화번호부에 없습니다. 출력
    void deleteInformation(String name) throws SQLException {
        //실행할 쿼리문
        stm = con.prepareStatement("delete from 전화번호부 where name = ?;");
        stm.setString(1, name);

        //executeUpdate -> 데이터가 잘 들어갔으면 1, 데이터가 들어가지 않았으면 0
        int result = stm.executeUpdate();
        if(result == 0){
            System.out.println("전화번호부에 없습니다.");
        }
    }

    // 2, 3의 데이터 검색, 삭제를 위한 이름 검색 메소드
    String findInformation() {
        Scanner sc = new Scanner(System.in);
        System.out.print("이름 : ");
        return sc.next();
    }

    void nameCheck(){

    }

    //4. 데이터 전체 출력
    void selectAll() throws SQLException {
        //실행할 쿼리문
        stm = con.prepareStatement("SELECT * FROM 전화번호부;");
        //쿼리문 반영
        ResultSet rs = stm.executeQuery();

        //전체 행 출력. ResultSet 커서를 다음 행으로 이동 하는 메소드
        //true면 ResultSet 커서 위치의 처리 행이 있는 경우의 반환값
        //false면 ResultSet 커서 위치의 처리 행이 없는 경우의 반환값 - 종료, EOF(End Of File)
        while (rs.next()) {
            System.out.print("이름 : ");
            System.out.println(rs.getString("name"));
            System.out.print("전화 번호 : ");
            System.out.println(rs.getString("phoneNumber"));
            System.out.print("주소 : ");
            System.out.println(rs.getString("address"));
            System.out.println();
        }
    }
}

//1. 데이터 입력
class InputClass {
    // 입력한 데이터 값 리턴
    Phone_Data dataReturn() {
        Phone_Data phone_data = new Phone_Data();

        Scanner sc = new Scanner(System.in);

        System.out.print("이름 입력 : ");
        phone_data.setName(sc.nextLine());
        System.out.print("전화 번호 입력 : ");
        phone_data.setPhoneNumber(sc.nextLine());
        System.out.print("주소 입력 : ");
        phone_data.setAddress(sc.nextLine());

        return phone_data;
    }
}


public class Phone {
    public static void main(String[] args) throws SQLException {
        Scanner sc = new Scanner(System.in);

        SQLC sq = new SQLC();

        InputClass ic = new InputClass();

        while (true) {
            System.out.println("<전화번호부 프로그램>");
            System.out.print("1. 입력 2. 검색 3. 삭제 4. 출력 5. 종료 : ");
            int num = sc.nextInt();

            //1. 입력
            if (num == 1) {
                sq.dataInsert(ic.dataReturn());
                sq.selectAll();
            }
            //2. 검색
            else if (num == 2) {
                sq.searchInformation(sq.findInformation());
                //검색된 데이터 출력
                sq.selectAll();
            }
            //3. 삭제
            else if (num == 3) {
                sq.deleteInformation(sq.findInformation());
                //삭제된 데이터 출력
                sq.selectAll();
            }
            //4. 출력
            else if (num == 4) {
                sq.selectAll();
            }
            //5. 종료
            else {
                System.out.println("프로그램을 종료합니다.");
                break;
            }
        }
    }

}
