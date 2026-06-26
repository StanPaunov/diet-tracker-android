package com.stanislav.diettracker;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputType;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends Activity {
    private static final int BG = Color.rgb(247, 244, 239);
    private static final int SURFACE = Color.WHITE;
    private static final int TEXT = Color.rgb(35, 43, 38);
    private static final int MUTED = Color.rgb(93, 103, 96);
    private static final int GREEN = Color.rgb(27, 138, 90);
    private static final int TEAL = Color.rgb(19, 116, 128);
    private static final int RED = Color.rgb(175, 63, 55);

    private final String[] tabs = {"Днес", "Седмица", "Правила", "Измервания"};
    private final String[] mealNames = {"Закуска", "Междинно", "Обяд", "Следобедна", "Вечеря"};
    private int selectedTab = 0;
    private int selectedDay = 0;
    private LinearLayout content;
    private LinearLayout nav;
    private SharedPreferences prefs;

    private final String[][] meals = {
        {"Овесени ядки с кисело мляко 2%, ябълка или боровинки, 1 ч.л. смляно ленено семе.", "Шепа сурови орехи или бадеми, около 20 г.", "Зеленчукова супа без запръжка, пълнозърнеста филия, салата от краставици, домати и магданоз със зехтин.", "Кефир или кисело мляко.", "Печено пилешко филе 120-150 г, задушени тиквички и моркови, малка порция кафяв ориз."},
        {"Пълнозърнеста филия с извара, домат и краставица, билков чай.", "1 круша.", "Елда със зеленчуци, салата от зеле и моркови, кисело мляко.", "1 портокал или мандарини.", "Омлет от 1 цяло яйце + 2 белтъка със спанак, салата, малка филия пълнозърнест хляб."},
        {"Кисело мляко с овесени ядки и канела, няколко череши или горски плодове.", "Моркови или краставици на пръчици.", "Пуешко филе около 120 г, печени зеленчуци, салата с лимон и зехтин.", "Извара с малко копър.", "Зеленчукова яхния с картоф, тиквичка, морков и чушка, салата от домати. Без месо."},
        {"Пълнозърнеста филия с тънък слой авокадо, варено яйце, домат.", "Ябълка.", "Малка порция леща или боб, ако подаграта е спокойна. При активни пристъпи замени с ориз/картоф със зеленчуци. Салата от зеле и кисело мляко.", "Шепа сурови ядки без сол.", "Печена бяла риба около 120 г, варени картофи, салата от краставици и копър."},
        {"Овесена каша с нискомаслено мляко или вода, канела, половин банан или ябълка.", "Кисело мляко.", "Пълнозърнеста паста със зеленчуков сос, салата от рукола/маруля, краставица и домат, малко настъргано сирене ако не е много солено.", "Череши, ако са сезонни, или горски плодове.", "Пилешко филе или пуешко месо, задушен броколи/карфиол, салата."},
        {"Извара с домат и краставица, пълнозърнеста филия, чай.", "1 плод по избор.", "Зеленчукова крем супа без сметана, печен картоф, салата със зехтин.", "Кефир или айрян без сол.", "Ризото с кафяв ориз и зеленчуци, салата. Без месо."},
        {"Кисело мляко с овес, ленено семе и плод, билков чай.", "Сурови ядки, около 15-20 г.", "Пуешко или пилешко месо 120-150 г, голяма салата, малка порция елда/кафяв ориз.", "Ябълка или круша.", "Зеленчукова запеканка с тиквички, моркови, картоф и малко извара, салата."}
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs = getSharedPreferences("diet_state", MODE_PRIVATE);
        selectedDay = prefs.getInt("selected_day", currentDayIndex());
        buildUi();
    }

    private void buildUi() {
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setBackgroundColor(BG);
        setContentView(root);

        LinearLayout header = new LinearLayout(this);
        header.setOrientation(LinearLayout.VERTICAL);
        header.setPadding(dp(20), dp(18), dp(20), dp(10));
        root.addView(header, new LinearLayout.LayoutParams(-1, -2));
        header.addView(text("Диета Контрол", 28, TEXT, true));
        header.addView(text("DASH + средиземноморски режим, адаптиран при риск от подагра", 14, MUTED, false));

        ScrollView scroll = new ScrollView(this);
        content = new LinearLayout(this);
        content.setOrientation(LinearLayout.VERTICAL);
        content.setPadding(dp(16), dp(8), dp(16), dp(96));
        scroll.addView(content);
        root.addView(scroll, new LinearLayout.LayoutParams(-1, 0, 1));

        nav = new LinearLayout(this);
        nav.setOrientation(LinearLayout.HORIZONTAL);
        nav.setPadding(dp(8), dp(8), dp(8), dp(8));
        nav.setBackgroundColor(Color.WHITE);
        root.addView(nav, new LinearLayout.LayoutParams(-1, -2));
        renderNav();
        renderContent();
    }

    private void renderNav() {
        nav.removeAllViews();
        for (int i = 0; i < tabs.length; i++) {
            final int index = i;
            Button button = actionButton(tabs[i], index == selectedTab ? GREEN : BG, index == selectedTab ? Color.WHITE : TEXT);
            button.setOnClickListener(v -> {
                selectedTab = index;
                renderNav();
                renderContent();
            });
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, dp(48), 1);
            lp.setMargins(dp(4), 0, dp(4), 0);
            nav.addView(button, lp);
        }
    }

    private void renderContent() {
        content.removeAllViews();
        if (selectedTab == 0) renderToday();
        if (selectedTab == 1) renderWeek();
        if (selectedTab == 2) renderRules();
        if (selectedTab == 3) renderMetrics();
    }

    private void renderToday() {
        addDayPicker();
        addCard("Ден " + (selectedDay + 1), "Отбелязвай храненията и водата за деня. Прогресът се пази само на телефона.");
        int checked = 0;
        for (int i = 0; i < mealNames.length; i++) {
            if (prefs.getBoolean(key("meal_" + i), false)) checked++;
        }
        ProgressBar bar = new ProgressBar(this, null, android.R.attr.progressBarStyleHorizontal);
        bar.setMax(mealNames.length);
        bar.setProgress(checked);
        content.addView(bar, new LinearLayout.LayoutParams(-1, -2));
        content.addView(text(checked + " от " + mealNames.length + " хранения са отбелязани", 14, MUTED, false));

        for (int i = 0; i < mealNames.length; i++) {
            final int mealIndex = i;
            LinearLayout card = card();
            CheckBox box = new CheckBox(this);
            box.setText(mealNames[i]);
            box.setTextSize(18);
            box.setTypeface(Typeface.DEFAULT_BOLD);
            box.setTextColor(TEXT);
            box.setChecked(prefs.getBoolean(key("meal_" + i), false));
            box.setOnCheckedChangeListener((buttonView, isChecked) -> {
                prefs.edit().putBoolean(key("meal_" + mealIndex), isChecked).apply();
                renderContent();
            });
            card.addView(box);
            TextView body = text(meals[selectedDay][i], 15, TEXT, false);
            body.setPadding(dp(38), 0, 0, 0);
            card.addView(body);
            content.addView(card, marginParams());
        }

        LinearLayout water = card();
        int cups = prefs.getInt(key("water"), 0);
        water.addView(text("Вода", 18, TEXT, true));
        water.addView(text(cups + " чаши / цел 8-10", 22, TEAL, true));
        LinearLayout controls = new LinearLayout(this);
        controls.setOrientation(LinearLayout.HORIZONTAL);
        Button minus = actionButton("- чаша", BG, TEXT);
        Button plus = actionButton("+ чаша", GREEN, Color.WHITE);
        minus.setOnClickListener(v -> updateWater(-1));
        plus.setOnClickListener(v -> updateWater(1));
        controls.addView(minus, new LinearLayout.LayoutParams(0, dp(48), 1));
        controls.addView(plus, new LinearLayout.LayoutParams(0, dp(48), 1));
        water.addView(controls);
        content.addView(water, marginParams());
        addWarning();
    }

    private void renderWeek() {
        addDayPicker();
        for (int d = 0; d < meals.length; d++) {
            LinearLayout card = card();
            card.addView(text("Ден " + (d + 1), 20, d == selectedDay ? GREEN : TEXT, true));
            for (int i = 0; i < mealNames.length; i++) {
                card.addView(text(mealNames[i] + ": " + meals[d][i], 14, TEXT, false));
            }
            final int day = d;
            card.setOnClickListener(v -> {
                selectedDay = day;
                prefs.edit().putInt("selected_day", selectedDay).apply();
                selectedTab = 0;
                renderNav();
                renderContent();
            });
            content.addView(card, marginParams());
        }
    }

    private void renderRules() {
        addSection("Препоръчителни храни", GREEN, new String[]{"Зеленчуци, особено листни, краставици, тиквички, моркови, броколи и чушки.", "Плодове: ябълки, круши, горски плодове, цитруси и череши.", "Пълнозърнести: овес, елда, кафяв ориз и пълнозърнест хляб.", "Нискомаслени млечни: кисело мляко, извара и кефир.", "Пиле/пуйка без кожа умерено; бяла риба 1-2 пъти седмично."});
        addSection("Да се ограничи или избягва", RED, new String[]{"Алкохол, особено бира и концентрати.", "Червено месо, карантии, колбаси, бекон и пастърма.", "Сардини, аншоа, херинга, миди и скариди.", "Захарни напитки, фруктозни сиропи и много сладкиши.", "Много солени храни, готови супи, чипс, консерви и полуфабрикати."});
        addSection("Порции и навици", TEAL, new String[]{"Месо/риба: 120-150 г, не всеки ден.", "Ориз/елда/паста: около 1/2-1 чаша сготвен продукт.", "Зеленчуци: поне половината чиния.", "Вода: около 2-2.5 л дневно, ако няма лекарско ограничение.", "Движение: 30 минути ходене поне 5 дни седмично."});
        addWarning();
    }

    private void renderMetrics() {
        addCard("Измервания", "Записвай показатели за проследяване: тегло, кръвно, пикочна киселина и кратка бележка.");
        addInput("Тегло, кг", "weight", false);
        addInput("Кръвно налягане", "bp", true);
        addInput("Пикочна киселина", "uric", false);
        addInput("Бележка", "note", true);
        content.addView(text("Последна промяна: " + prefs.getString("metrics_saved", "няма запис"), 13, MUTED, false));
        Button save = actionButton("Запази измерванията", GREEN, Color.WHITE);
        save.setOnClickListener(v -> {
            String now = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault()).format(new Date());
            prefs.edit().putString("metrics_saved", now).apply();
            renderContent();
        });
        content.addView(save, new LinearLayout.LayoutParams(-1, dp(52)));
        addWarning();
    }

    private void addDayPicker() {
        HorizontalScrollView hsv = new HorizontalScrollView(this);
        hsv.setHorizontalScrollBarEnabled(false);
        LinearLayout days = new LinearLayout(this);
        days.setOrientation(LinearLayout.HORIZONTAL);
        hsv.addView(days);
        for (int i = 0; i < 7; i++) {
            final int day = i;
            Button b = actionButton("Ден " + (i + 1), i == selectedDay ? GREEN : SURFACE, i == selectedDay ? Color.WHITE : TEXT);
            b.setOnClickListener(v -> {
                selectedDay = day;
                prefs.edit().putInt("selected_day", selectedDay).apply();
                renderContent();
            });
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(dp(92), dp(44));
            lp.setMargins(0, 0, dp(8), dp(10));
            days.addView(b, lp);
        }
        content.addView(hsv);
    }

    private void addInput(String label, String name, boolean textMode) {
        LinearLayout box = card();
        box.addView(text(label, 14, MUTED, false));
        EditText input = new EditText(this);
        input.setText(prefs.getString("metric_" + name, ""));
        input.setTextColor(TEXT);
        input.setTextSize(18);
        input.setMinLines(name.equals("note") ? 3 : 1);
        input.setInputType(textMode ? InputType.TYPE_CLASS_TEXT : InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        input.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) prefs.edit().putString("metric_" + name, input.getText().toString()).apply();
        });
        box.addView(input);
        content.addView(box, marginParams());
    }

    private void addSection(String title, int color, String[] items) {
        LinearLayout section = card();
        section.addView(text(title, 20, color, true));
        for (String item : items) section.addView(text("• " + item, 15, TEXT, false));
        content.addView(section, marginParams());
    }

    private void addWarning() {
        addCard("Важно", "Приложението е помощник за проследяване, не медицинска диагноза. При подагра, високо кръвно, диабет, бъбречен проблем или лекарства режимът трябва да се съгласува с лекар/диетолог.");
    }

    private void addCard(String title, String body) {
        LinearLayout card = card();
        card.addView(text(title, 19, TEXT, true));
        card.addView(text(body, 14, MUTED, false));
        content.addView(card, marginParams());
    }

    private LinearLayout card() {
        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setPadding(dp(16), dp(14), dp(16), dp(14));
        card.setBackgroundColor(SURFACE);
        return card;
    }

    private TextView text(String value, int sp, int color, boolean bold) {
        TextView t = new TextView(this);
        t.setText(value);
        t.setTextSize(sp);
        t.setTextColor(color);
        t.setLineSpacing(dp(2), 1.0f);
        if (bold) t.setTypeface(Typeface.DEFAULT_BOLD);
        return t;
    }

    private Button actionButton(String value, int bg, int color) {
        Button b = new Button(this);
        b.setText(value);
        b.setAllCaps(false);
        b.setTextSize(14);
        b.setTextColor(color);
        b.setBackgroundColor(bg);
        return b;
    }

    private void updateWater(int delta) {
        int next = Math.max(0, Math.min(12, prefs.getInt(key("water"), 0) + delta));
        prefs.edit().putInt(key("water"), next).apply();
        renderContent();
    }

    private LinearLayout.LayoutParams marginParams() {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-1, -2);
        lp.setMargins(0, 0, 0, dp(12));
        return lp;
    }

    private String key(String name) {
        return "day_" + selectedDay + "_" + name;
    }

    private int currentDayIndex() {
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        int day = calendar.get(java.util.Calendar.DAY_OF_WEEK);
        return day == java.util.Calendar.SUNDAY ? 6 : day - 2;
    }

    private int dp(int value) {
        return (int) (value * getResources().getDisplayMetrics().density + 0.5f);
    }
}
