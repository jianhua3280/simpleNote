package com.example.sjhapp_note;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    public ArrayList<String> dataSource = new ArrayList<>();
    public ArrayList<String> delSource = new ArrayList<>();


    DBhelper dBhelper;
    SQLiteDatabase db;
    MyAdapter myAdapter_new;
    MyAdapterOld myAdapter_old;
    Dialog_new dialog_new;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        每次启动读取数据库文件到数组
        readSource();

        RecyclerView rView_new = findViewById(R.id.id_rView_new);
        myAdapter_new = new MyAdapter(dataSource);
        rView_new.setAdapter(myAdapter_new);
        rView_new.setLayoutManager(new LinearLayoutManager(this));
        RecyclerView rView_old = findViewById(R.id.id_rView_old);
        myAdapter_old = new MyAdapterOld(delSource);
        rView_old.setAdapter(myAdapter_old);
        rView_old.setLayoutManager(new LinearLayoutManager(this));
//设置左右滑操作
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                int swiped =  ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT;

                return makeMovementFlags(0, swiped);
            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                if (direction == ItemTouchHelper.RIGHT){
                int position = viewHolder.getAdapterPosition();
                delSource.add(dataSource.get(dataSource.size() - 1 - position));
                dataSource.remove((dataSource.size() - 1 - position));
                myAdapter_new.notifyItemRemoved(position);
                myAdapter_old.notifyDataSetChanged();}else{
                    int position = viewHolder.getAdapterPosition();
                    String temp=dataSource.get(dataSource.size() - 1 - position);
                    dataSource.remove((dataSource.size() - 1 - position));
                    dataSource.add(temp);
                    myAdapter_new.notifyDataSetChanged();
                }
            }
        });
        itemTouchHelper.attachToRecyclerView(rView_new);
        ItemTouchHelper itemTouchHelper1 = new ItemTouchHelper(new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                int swiped = ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT;

                return makeMovementFlags(0, swiped);
            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                if (direction == ItemTouchHelper.RIGHT) {
                    int position = viewHolder.getAdapterPosition();
                    delSource.remove(delSource.size() - 1 - position);
                    myAdapter_old.notifyItemRemoved(position);
                } else {
                    int position = viewHolder.getAdapterPosition();
                    dataSource.add(delSource.get(delSource.size() - 1 - position));

                    delSource.remove(delSource.size() - 1 - position);
                    myAdapter_old.notifyItemRemoved(position);
                    myAdapter_new.notifyDataSetChanged();

                }

            }
        });
        itemTouchHelper1.attachToRecyclerView(rView_old);


    }

    //每次暂停时存入数据库
    //用来测试git

    @Override
    protected void onPause() {
        writeSource();
        super.onPause();
    }

    public void addDialog(View view) {
        dialog_new = new Dialog_new();
        dialog_new.show(getSupportFragmentManager(), "Dialog_new");

    }

    public void dialog_addNote(View view) {
        String addText = dialog_new.dialogEdit.getText().toString().trim();
        if (addText.equals("")) {
            dialog_new.dismiss();
            Toast.makeText(this, "无新增事件", Toast.LENGTH_SHORT).show();
        } else {
            dataSource.add(addText);
            dialog_new.dismiss();
            myAdapter_new.notifyDataSetChanged();


        }


    }

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
        ArrayList<String> resault = new ArrayList<>();

        public MyAdapter(ArrayList<String> resault) {
            this.resault = resault;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_new, parent, false);
            MyViewHolder myViewHolder = new MyViewHolder(view);


            return myViewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
            holder.mText.setText(resault.get(resault.size() - 1 - position));
            Random random = new Random();
            int r = random.nextInt(256);
            int g = random.nextInt(256);
            int b = random.nextInt(256);
            holder.mText.setBackgroundColor(Color.rgb(r, g, b));
            holder.mText.setTextColor(Color.rgb(((r + 128) % 255), ((g + 128) % 255), ((b + 128) % 255)));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String temp = holder.mText.getText().toString();
                    dataSource.remove(dataSource.size() - 1 - position);
                    dialog_new = new Dialog_new(temp);
                    dialog_new.show(getSupportFragmentManager(), "Dialog_new");
                }
            });
        }

        @Override
        public int getItemCount() {
            return resault.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            TextView mText;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                mText = itemView.findViewById(R.id.id_item_textView);
            }
        }
    }

    public class MyAdapterOld extends RecyclerView.Adapter<MyAdapterOld.MyViewHolder1> {
        ArrayList<String> resault = new ArrayList<>();

        public MyAdapterOld(ArrayList<String> resault) {
            this.resault = resault;
        }

        @NonNull
        @Override
        public MyAdapterOld.MyViewHolder1 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_old, parent, false);
            MyViewHolder1 myViewHolder1 = new MyViewHolder1(view);


            return myViewHolder1;
        }

        @Override
        public void onBindViewHolder(@NonNull final MyAdapterOld.MyViewHolder1 holder, final int position) {
            holder.mText.setText(resault.get(resault.size() - 1 - position));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String temp = holder.mText.getText().toString();
                    delSource.remove(delSource.size() - 1 - position);
                    dialog_new = new Dialog_new(temp);
                    dialog_new.show(getSupportFragmentManager(), "Dialog_new");
                }
            });

        }

        @Override
        public int getItemCount() {
            return resault.size();
        }

        class MyViewHolder1 extends RecyclerView.ViewHolder {
            TextView mText;

            public MyViewHolder1(@NonNull View itemView) {
                super(itemView);
                mText = itemView.findViewById(R.id.id_itemOld_text);
            }
        }
    }

    //    启动读取数据到Source的方法
    public void readSource() {


        dBhelper = new DBhelper(MainActivity.this, "test_db", null, 1);
        db = dBhelper.getWritableDatabase();

        Cursor cursor = db.query("user", new String[]{"data"}, null, null, null, null, null);
        while (cursor.moveToNext()) {
            String data = cursor.getString(cursor.getColumnIndex("data"));
            if(data!=null)
            dataSource.add(data);
        }
        Cursor cursor1 = db.query("user", new String[]{"del"}, null, null, null, null, null);
        while (cursor1.moveToNext()) {
            String del = cursor1.getString(cursor1.getColumnIndex("del"));
            if(del!=null)
            delSource.add(del);
        }





    }

    //    关闭写入数据到数据库的方法
    public void writeSource() {
        dBhelper = new DBhelper(MainActivity.this, "test_db", null, 1);
        db = dBhelper.getWritableDatabase();
        db.execSQL("DELETE FROM user" );
        ContentValues value = new ContentValues();
        ContentValues value1 = new ContentValues();

        for (int i = 0; i < dataSource.size(); i++) {
            value.put("data", dataSource.get(i));
            db.insert("user", "del", value);

        }
        for (int k = 0; k < delSource.size(); k++) {
            value1.put("del", delSource.get(k));
            db.insert("user", "data", value1);

        }
        db.close();

    }
}