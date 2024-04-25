package com.example.destination.utils;

import android.content.Context;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class MemoryData {
    public static  void saveData(String data, Context context) throws IOException {
        try {
            FileOutputStream fileOutputStream = context.openFileOutput("data.txt", Context.MODE_PRIVATE);
            fileOutputStream.write(data.getBytes());
            fileOutputStream.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public static  void saveLastMsgTS   (String data,String chatId,Context context) throws IOException {
        try {
            FileOutputStream fileOutputStream = context.openFileOutput(chatId + ".txt", Context.MODE_PRIVATE);
            fileOutputStream.write(data.getBytes());
            fileOutputStream.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    public static  void saveName(String data, Context context) throws IOException {
        try {
            FileOutputStream fileOutputStream = context.openFileOutput("name .txt", Context.MODE_PRIVATE);
            fileOutputStream.write(data.getBytes());
            fileOutputStream.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    public static  String getData(Context context){
        String data = "";
        try{
            FileInputStream  fis = context.openFileInput("data.txt");
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String line;
            while((line = bufferedReader.readLine()) != null){
                sb.append(line);
            }
            data = sb.toString();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return data;
    }
    public static  String getlastMsgTS(Context context,String chatId){
        String data = "0";
        try{
            FileInputStream  fis = context.openFileInput(chatId + ".txt");
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String line;
            while((line = bufferedReader.readLine()) != null){
                sb.append(line);
            }
            data = sb.toString();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return data;
    }
    public static  String getName(Context context){
        String data = "";
        try{
            FileInputStream  fis = context.openFileInput("name.txt");
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String line;
            while((line = bufferedReader.readLine()) != null){
                sb.append(line);
            }
            data = sb.toString();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return data;
    }

}
