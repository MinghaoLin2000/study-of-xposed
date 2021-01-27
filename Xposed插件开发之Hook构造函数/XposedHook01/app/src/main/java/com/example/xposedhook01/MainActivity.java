package com.example.xposedhook01;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {
    public void printStudent(Student stu)
    {
        Log.i("Xposed", stu.name+"- -"+stu.id+"- - - "+stu.age);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Student astudent=new Student();
        Student bstudent=new Student("xiaoming");
        Student cstudent=new Student("xiaohua","2020");
        Student dstudent=new Student("xiaohong","2020",20);
        printStudent(astudent);
        printStudent(bstudent);
        printStudent(cstudent);
        printStudent(dstudent);
    }
}