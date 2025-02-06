package com.shop2.service;

import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;

//파일을 처리하는 FileService 클래스. 파일을 업로드하는 메소드와 삭제하는 메소드
@Service  // @Service 어노테이션은 Spring의 서비스 계층을 의미하며 이 클래스가 비즈니스 로직을 처리하는 서비스 클래스임을 나타낸다 스프링 컨테이너가 해당 클래스를 Bean으로 등록하여 의존성 주입을 가능하게 한다.
@Log
public class FileService {  //uploadpath : 파일을 저장할 디렉터리 경로, originalFilename :  업로드된 파일의 원본 파일명, fileData : 파일의 바이트 배열(실제 파일 데이터)
    public String uploadFile(String uploadPath, String originalFilename, byte[] fileData) throws Exception {
        UUID uuid = UUID.randomUUID(); // Universally Unique Identifier는 서로 다른 개체들을 구별하기 위해서 이름을 부여할 때 사용된다. 실제 사용 시 중복될 가능성이 거의 없기 때문에 파일의 이름으로 사용하면 파일명 중복 문제를 해결할 수 있다.
        String extension = originalFilename.substring(originalFilename.lastIndexOf(".")); //원본 파일명에서 확장자 부분만 추출. lastUbdexOf(".")를 사용하여 마지막 . 이후의 문자열 확장자만 가져온다
        String savedFileName = uuid.toString() + extension;  // 새로 생성한 uuid를 문자열로 변환한 후 기존 파일의 확장자를 추가하여 새로운 파일명을 생성한다
        String fileUploadFullUrl = uploadPath + "/" + savedFileName;  // 전체 경로 설정
        FileOutputStream fos = new FileOutputStream(fileUploadFullUrl); // FileOutputStream을 생성하여 파일을 저장할 출력 스트림을 생성
        fos.write(fileData); // 파일 데이터를 해당 경로로 씀
        fos.close();  // 파일 스트림을 닫는다 자원을 낭비하지 않기 위한
        return savedFileName;
    }
    public void deleteFile(String filePath) throws Exception {
        File deleteFile = new File(filePath);

        if (deleteFile.exists()) {
            deleteFile.delete();
            log.info("파일을 삭제했습니다.");
        } else {
            log.info("파일이 존재하지 않습니다.");
        }
    }
}
