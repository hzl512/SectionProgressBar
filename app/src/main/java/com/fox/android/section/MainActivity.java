package com.fox.android.section;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.fox.android.section.databinding.ActivityMainBinding;
import com.fox.android.section.progress.bar.SectionProgressBar;
import com.fox.android.section.progress.bar.ShaftRegionItem;
import com.zyyoona7.popup.EasyPopup;
import com.zyyoona7.popup.XGravity;
import com.zyyoona7.popup.YGravity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 10001049
 */
public class MainActivity extends AppCompatActivity {

    private final static String TAG = "MainActivity";

    private List<String> timeList1 = new ArrayList<>();
    private List<String> timeList2 = new ArrayList<>();
    private List<String> timeList3 = new ArrayList<>();
    private List<String> timeList4 = new ArrayList<>();

    private List<List<String>> timesList = new ArrayList<>();
    private ArrayList<ShaftRegionItem> shaftRegionItemList = new ArrayList<>();

    private ActivityMainBinding dataBind;

    private EasyPopup popupSection, arrowPopupSection;
    private TextView tvStart, tvEnd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataBind = DataBindingUtil.setContentView(this, R.layout.activity_main);

        long a = System.currentTimeMillis();

        timeList1.add("2020-09-01 00:00:00");
        timeList1.add("2020-09-01 02:00:00");

        timeList2.add("2020-09-01 09:33:59");
        timeList2.add("2020-09-01 10:33:10");

        timeList3.add("2020-09-01 15:33:59");
        timeList3.add("2020-09-01 16:35:15");

        timeList4.add("2020-09-01 22:00:00");
        timeList4.add("2020-09-01 23:59:59");

        timesList.add(timeList1);
        timesList.add(timeList2);
        timesList.add(timeList3);
        timesList.add(timeList4);

        for (List<String> times : timesList) {
            String time1 = times.get(0);
            String time2 = times.get(1);

            ShaftRegionItem shaftRegionItem = new ShaftRegionItem();
            shaftRegionItem.setStartSection(TimeUtils.string2Millis(time1));
            shaftRegionItem.setEndSection(TimeUtils.string2Millis(time2));

            shaftRegionItem.setStartSectionStr(time1);
            shaftRegionItem.setEndSectionStr(time2);

            shaftRegionItemList.add(shaftRegionItem);
        }

        popupSection = EasyPopup.create()
                .setContentView(this, R.layout.arrow_section_pop)
//                .setAnimationStyle(R.style.TopPopAnim)
                .setBackgroundDimEnable(false)
                .setDimValue(0.4f)
                .setFocusAndOutsideEnable(true)
                .apply();

        arrowPopupSection = EasyPopup.create()
                .setContentView(this, R.layout.arrow_section_pop1)
//                .setAnimationStyle(R.style.TopPopAnim)
                .setBackgroundDimEnable(false)
                .setDimValue(0.4f)
                .setFocusAndOutsideEnable(true)
                .apply();

        View arrowView = arrowPopupSection.findViewById(R.id.vArrow);
        arrowView.setBackground(new TriangleDrawable(TriangleDrawable.TOP, Color.parseColor("#16A7EF")));

        tvStart = popupSection.findViewById(R.id.tvStart);
        tvEnd = popupSection.findViewById(R.id.tvEnd);

        dataBind.sectionProgressBar.setShaftItems(shaftRegionItemList, TimeUtils.string2Millis("2020-09-01", "yyyy-MM-dd"));
        Log.e(TAG, "用时：" + (System.currentTimeMillis() - a));

        dataBind.sectionProgressBar.setClickSectionListener(new SectionProgressBar.ClickSectionListener() {

            @Override
            public void clickBar(ShaftRegionItem item, int viewY) {
                tvStart.setText(item.getStartSectionStr());
                tvEnd.setText(" ~ " + item.getEndSectionStr());

                int x = (int) item.getStartX() + (int) (Math.floor(item.getEndX() - item.getStartX())) / 2;

                Log.e(TAG, "StartX：" + x);

                arrowPopupSection.showAsDropDown(dataBind.sectionProgressBar, x, 0);

                popupSection.showAsDropDown(dataBind.sectionProgressBar,
                        x -(int) (Math.floor(item.getEndX() - item.getStartX())) / 2, SizeUtils.dp2px(7));

//                popupSection.showAtLocation(dataBind.timeShaftRegionProgressBar, YGravity.BELOW, x, 0);
//                popupSection.showAtAnchorView(dataBind.timeShaftRegionProgressBar, YGravity.BELOW, XGravity.LEFT, x, 0);

            }
        });

    }

}