package com.lab;

import java.io.*;
import java.nio.file.*;

public class HighLevelandScoreManager {

    private static final String FILE_NAME = "highslevel.txt";

    // ฟังก์ชันอ่าน high score จากไฟล์
    public static int readHighLevelandScore() {
        try {
            Path filePath = Paths.get(FILE_NAME);
            if (Files.exists(filePath)) {
                String content = new String(Files.readAllBytes(filePath));
                return Integer.parseInt(content); // อ่านค่าจากไฟล์
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0; // ถ้าไฟล์ไม่มีหรือเกิดข้อผิดพลาดให้ส่งค่า 0
    }

    // ฟังก์ชันสำหรับบันทึก high level ลงไฟล์
    public static void writeHighLevel(int level) {
        try {
            Files.write(Paths.get(FILE_NAME), String.valueOf(level).getBytes(), StandardOpenOption.CREATE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}