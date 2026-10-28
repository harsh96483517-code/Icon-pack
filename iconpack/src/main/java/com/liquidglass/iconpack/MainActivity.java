package com.liquidglass.iconpack;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.res.XmlResourceParser;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;
import org.xmlpull.v1.XmlPullParser;

public class MainActivity extends Activity {
    private final List<String> all = new ArrayList<>();
    private final List<String> shown = new ArrayList<>();
    private IconAdapter adapter;
    private int pad;

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        loadNames();
        float d = getResources().getDisplayMetrics().density;
        pad = (int) (12 * d);

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setBackgroundColor(Color.WHITE);
        root.setPadding(pad, pad * 2, pad, 0);

        TextView title = new TextView(this);
        title.setText("Liquid Glass icon pack");
        title.setTextSize(20);
        title.setTextColor(Color.BLACK);
        root.addView(title);

        TextView sub = new TextView(this);
        sub.setText(all.size() + " icons. Launcher ki icon-pack setting se apply karo, "
                + "ya kisi bhi icon ko long-press karke is pack se icon choose karo.");
        sub.setTextSize(12);
        sub.setTextColor(Color.DKGRAY);
        sub.setPadding(0, pad / 2, 0, pad);
        root.addView(sub);

        Button apply = new Button(this);
        apply.setText("Apply in launcher");
        apply.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) { applyPack(); }
        });
        root.addView(apply);

        EditText search = new EditText(this);
        search.setHint("Search icons");
        search.setSingleLine(true);
        root.addView(search);

        GridView grid = new GridView(this);
        grid.setNumColumns(4);
        grid.setVerticalSpacing(pad);
        grid.setHorizontalSpacing(pad / 2);
        adapter = new IconAdapter();
        grid.setAdapter(adapter);
        root.addView(grid, new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 0, 1f));

        search.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int a, int b, int c) { }
            public void onTextChanged(CharSequence s, int a, int b, int c) { filter(s.toString()); }
            public void afterTextChanged(Editable s) { }
        });

        setContentView(root);
    }

    private void loadNames() {
        try {
            XmlResourceParser p = getResources().getXml(R.xml.drawable);
            int ev;
            while ((ev = p.next()) != XmlPullParser.END_DOCUMENT) {
                if (ev == XmlPullParser.START_TAG && "item".equals(p.getName())) {
                    String n = p.getAttributeValue(null, "drawable");
                    if (n != null) all.add(n);
                }
            }
            p.close();
        } catch (Exception ignored) { }
        shown.addAll(all);
    }

    private void filter(String q) {
        String s = q == null ? "" : q.trim().toLowerCase();
        shown.clear();
        if (s.isEmpty()) {
            shown.addAll(all);
        } else {
            for (String n : all) {
                if (n.replace('_', ' ').contains(s)) shown.add(n);
            }
        }
        if (adapter != null) adapter.notifyDataSetChanged();
    }

    private int iconId(String name) {
        return getResources().getIdentifier(name, "drawable", getPackageName());
    }

    private void applyPack() {
        String[][] launchers = {
                {"com.teslacoilsw.launcher", "com.teslacoilsw.launcher.APPLY_ICON_THEME"},
                {"org.adw.launcher", "org.adw.launcher.APPLY_THEME"},
                {"com.gau.go.launcherex", "com.gau.go.launcherex.theme"},
                {"ginlemon.flowerfree", "ginlemon.smartlauncher.APPLY_THEME"},
        };
        for (String[] l : launchers) {
            try {
                Intent i = new Intent(l[1]);
                i.setPackage(l[0]);
                i.putExtra("com.teslacoilsw.launcher.extra.ICON_THEME_PACKAGE", getPackageName());
                i.putExtra("icon_pack_package", getPackageName());
                i.putExtra("package_name", getPackageName());
                startActivity(i);
                return;
            } catch (ActivityNotFoundException ignored) { }
        }
        Toast.makeText(this,
                "Launcher ki settings kholo > Icon pack / Look & feel > Liquid Glass chuno",
                Toast.LENGTH_LONG).show();
    }

    private class IconAdapter extends BaseAdapter {
        public int getCount() { return shown.size(); }
        public Object getItem(int pos) { return shown.get(pos); }
        public long getItemId(int pos) { return pos; }

        public View getView(int pos, View reuse, ViewGroup parent) {
            LinearLayout box = new LinearLayout(MainActivity.this);
            box.setOrientation(LinearLayout.VERTICAL);
            box.setGravity(Gravity.CENTER_HORIZONTAL);

            ImageView iv = new ImageView(MainActivity.this);
            int id = iconId(shown.get(pos));
            if (id != 0) iv.setImageResource(id);
            box.addView(iv, new LinearLayout.LayoutParams(pad * 4, pad * 4));

            TextView tv = new TextView(MainActivity.this);
            tv.setText(shown.get(pos).replace('_', ' '));
            tv.setTextSize(9);
            tv.setTextColor(Color.DKGRAY);
            tv.setGravity(Gravity.CENTER);
            tv.setMaxLines(2);
            box.addView(tv);
            return box;
        }
    }
}
