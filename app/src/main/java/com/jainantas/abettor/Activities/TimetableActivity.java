package com.jainantas.abettor.Activities;

import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.jainantas.abettor.Adapters.TImeTableNavAdapter;
import com.jainantas.abettor.Fragments.MedicineFragment;
import com.jainantas.abettor.Fragments.Timetable;
import com.jainantas.abettor.R;

public class TimetableActivity extends AppCompatActivity {
MenuItem prev;
BottomNavigationView nav;
ViewPager viewPager;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timetable);
        int n=getApplicationContext().getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        if(n==Configuration.UI_MODE_NIGHT_YES)
            findViewById(R.id.relative).setBackgroundColor(Color.BLACK);
        nav=findViewById(R.id.bottomNavigate);
        viewPager=findViewById(R.id.vp);
        nav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.tt:
                        viewPager.setCurrentItem(0);
                        return true;
                    case R.id.addMed:
                        viewPager.setCurrentItem(1);
                        return true;
                }
                return false;
            }
        });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(prev!=null)
                    prev.setChecked(false);
                else
                    nav.getMenu().getItem(0).setChecked(false);
                nav.getMenu().getItem(position).setChecked(true);
                prev=nav.getMenu().getItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        TImeTableNavAdapter adapter=new TImeTableNavAdapter(getSupportFragmentManager());
        Timetable timetable=new Timetable();
        MedicineFragment medicineFragment=new MedicineFragment();
        adapter.addFragment(timetable);
        adapter.addFragment(medicineFragment);
        viewPager.setAdapter(adapter);
    }
}
