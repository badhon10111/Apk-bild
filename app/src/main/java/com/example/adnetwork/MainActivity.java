package com.example.adnetwork;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.View;
import android.widget.*;

public class MainActivity extends Activity {
    final int navy = Color.rgb(36, 42, 135);
    final int ink = Color.rgb(25, 31, 48);
    final int muted = Color.rgb(112, 117, 130);
    final int bg = Color.rgb(245, 246, 250);
    LinearLayout root, content;
    FrameLayout frame;
    boolean drawerOpen = false, liked = false, autoPlay = false;
    int given = 0, received = 0, completed = 3, page = 1;
    TextView statsGiven, statsReceived, progressText, breakText, autoBadge, likeButton, playButton;

    @Override public void onCreate(Bundle b) {
        super.onCreate(b);
        getWindow().setStatusBarColor(bg);
        getWindow().setNavigationBarColor(Color.BLACK);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        showHome();
    }

    GradientDrawable shape(int color, int stroke, int radius) {
        GradientDrawable d = new GradientDrawable();
        d.setColor(color); d.setCornerRadius(radius);
        if (stroke != 0) d.setStroke(2, stroke);
        return d;
    }
    TextView text(String s, int size, int color, boolean bold) {
        TextView t = new TextView(this); t.setText(s); t.setTextSize(size); t.setTextColor(color);
        t.setGravity(Gravity.CENTER_VERTICAL);
        if (bold) t.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        return t;
    }
    LinearLayout column() {
        LinearLayout l = new LinearLayout(this); l.setOrientation(LinearLayout.VERTICAL); return l;
    }
    LinearLayout row() {
        LinearLayout l = new LinearLayout(this); l.setOrientation(LinearLayout.HORIZONTAL);
        l.setGravity(Gravity.CENTER_VERTICAL); return l;
    }
    void pad(View v, int l, int t, int r, int b) { v.setPadding(l,t,r,b); }
    LinearLayout.LayoutParams lp(int w, int h) { return new LinearLayout.LayoutParams(w,h); }
    void space(LinearLayout parent, int h) { View v = new View(this); parent.addView(v, lp(1,h)); }
    TextView button(String label, int color, int textColor) {
        TextView t = text(label, 15, textColor, true); t.setGravity(Gravity.CENTER);
        t.setPadding(16,12,16,12); t.setBackground(shape(color,0,28)); return t;
    }
    void showHome() {
        drawerOpen = false;
        frame = new FrameLayout(this);
        root = column(); root.setBackgroundColor(bg);
        frame.addView(root, new FrameLayout.LayoutParams(-1,-1));
        ScrollView scroll = new ScrollView(this);
        content = column(); content.setPadding(18,8,18,28);
        scroll.addView(content);
        root.addView(scroll, new LinearLayout.LayoutParams(-1,0,1));
        buildHeader();
        buildStats();
        buildCampaign();
        buildOverview();
        buildPost();
        setContentView(frame);
        TextView menu = (TextView) findViewById(1001);
        if (menu != null) menu.setOnClickListener(v -> showDrawer());
    }
    void buildHeader() {
        LinearLayout header = row(); header.setPadding(0,8,0,18);
        TextView menu = text("☰",30,ink,true); menu.setGravity(Gravity.CENTER); menu.setId(1001);
        header.addView(menu, lp(48,54));
        TextView logo = text("🅰",24,Color.WHITE,true); logo.setGravity(Gravity.CENTER);
        logo.setBackground(shape(navy,0,14)); header.addView(logo,lp(46,46));
        TextView brand = text("  AdNetwork",22,navy,true); header.addView(brand,new LinearLayout.LayoutParams(0,54,1));
        TextView adsterra = text("Adsterra",19,navy,true); header.addView(adsterra,lp(-2,54));
        content.addView(header);
        LinearLayout titleRow = row();
        titleRow.addView(text("Your Stats",20,ink,true),new LinearLayout.LayoutParams(0,48,1));
        TextView refresh = text("⟳",30,navy,true); refresh.setGravity(Gravity.CENTER);
        refresh.setOnClickListener(v -> Toast.makeText(this,"Stats refreshed (demo)",Toast.LENGTH_SHORT).show());
        titleRow.addView(refresh,lp(42,48)); content.addView(titleRow);
    }
    void buildStats() {
        LinearLayout card = row(); card.setBackground(shape(Color.WHITE,0xFFDDE0EA,24)); card.setPadding(12,18,12,18);
        LinearLayout left = column(); left.setGravity(Gravity.CENTER);
        statsGiven = text("👍  "+given,22,ink,true); statsGiven.setGravity(Gravity.CENTER);
        left.addView(statsGiven); left.addView(centerText("Given Today",15,muted));
        LinearLayout right = column(); right.setGravity(Gravity.CENTER);
        statsReceived = text("❤️  "+received,22,ink,true); statsReceived.setGravity(Gravity.CENTER);
        right.addView(statsReceived); right.addView(centerText("Received Today",15,muted));
        card.addView(left,new LinearLayout.LayoutParams(0,-2,1));
        View divider = new View(this); divider.setBackgroundColor(0xFFE2E4EA); card.addView(divider,lp(1,48));
        card.addView(right,new LinearLayout.LayoutParams(0,-2,1));
        content.addView(card); space(content,18);
    }
    TextView centerText(String s,int size,int color) { TextView t=text(s,size,color,false); t.setGravity(Gravity.CENTER); return t; }
    void buildCampaign() {
        LinearLayout card = row(); card.setPadding(20,18,18,18);
        GradientDrawable gd = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT,new int[]{0xFF242987,0xFF20505B});
        gd.setCornerRadius(30); card.setBackground(gd);
        TextView icon = centerText("🚀",26,Color.WHITE); icon.setBackground(shape(0x55FFFFFF,0,60));
        card.addView(icon,lp(58,58));
        LinearLayout words = column(); words.setPadding(14,0,0,0);
        words.addView(text("Ad Campaigns",20,Color.WHITE,true));
        words.addView(text("View sponsors & boost your score",14,0xFFE4E8FA,false));
        card.addView(words,new LinearLayout.LayoutParams(0,-2,1));
        card.addView(text("›",34,Color.WHITE,true));
        card.setOnClickListener(v -> pageMessage("Campaign","View sponsors and available campaigns here."));
        content.addView(card); space(content,18);
    }
    void buildOverview() {
        LinearLayout card = column(); card.setPadding(18,18,18,18); card.setBackground(shape(0xFFF9FAFC,0xFFD0D3E0,28));
        LinearLayout top = row(); top.addView(text("◧  Page "+page+" Overview",19,ink,true),new LinearLayout.LayoutParams(0,44,1));
        autoBadge = button(autoPlay ? "● AutoPlay On" : "● AutoPlay Idle",0xFFF8F8FA,autoPlay?0xFF25804A:ink);
        autoBadge.setTextSize(12); autoBadge.setOnClickListener(v -> toggleAuto()); top.addView(autoBadge); card.addView(top);
        progressText = text("Completed: "+completed+" of 10 links ("+(completed*10)+"%)",16,ink,false);
        LinearLayout.LayoutParams pp=lp(-1,42); pp.topMargin=4; card.addView(progressText,pp);
        ProgressBar bar = new ProgressBar(this,null,android.R.attr.progressBarStyleHorizontal);
        bar.setMax(10); bar.setProgress(completed); bar.setProgressTintList(android.content.res.ColorStateList.valueOf(navy));
        bar.setProgressBackgroundTintList(android.content.res.ColorStateList.valueOf(0xFFEDEEF3));
        card.addView(bar,lp(-1,10));
        LinearLayout breakRow=row(); breakRow.setPadding(12,18,4,0);
        breakRow.addView(text("☕  Break Progress:",14,muted,false),new LinearLayout.LayoutParams(0,34,1));
        breakText=text(given+" / 150 likes",14,navy,true); breakRow.addView(breakText); card.addView(breakRow);
        content.addView(card); space(content,22);
    }
    void buildPost() {
        LinearLayout card=column(); card.setPadding(18,18,18,18); card.setBackground(shape(Color.WHITE,0,28));
        LinearLayout author=row();
        TextView avatar=centerText("A$",20,0xFFFFD54F); avatar.setBackground(shape(0xFF171A32,0,50)); author.addView(avatar,lp(54,54));
        LinearLayout names=column(); names.setPadding(12,0,0,0);
        names.addView(text("AdNetwork",19,ink,true)); names.addView(text("◉  Published : 15 Oct 2026, 03:28 PM",12,muted,false));
        author.addView(names,new LinearLayout.LayoutParams(0,-2,1));
        TextView more=text("•••",22,muted,true); author.addView(more); card.addView(author);
        LinearLayout task=column(); task.setPadding(16,16,16,16); task.setBackground(shape(0xFFF7F8FC,0xFFE2E4EC,24));
        LinearLayout taskTop=row();
        TextView rocket=centerText("🚀",22,navy); rocket.setBackground(shape(0xFFE8EAFB,0,12)); taskTop.addView(rocket,lp(52,52));
        LinearLayout taskName=column(); taskName.setPadding(12,0,0,0);
        taskName.addView(text("Task ID",13,muted,false)); taskName.addView(text("#AD9951A7-62A...",16,ink,true));
        taskTop.addView(taskName,new LinearLayout.LayoutParams(0,-2,1));
        taskTop.addView(button("🌐 Unknown",0xFFE5F2E8,0xFF34764A)); task.addView(taskTop);
        View line=new View(this); line.setBackgroundColor(0xFFE3E5ED); LinearLayout.LayoutParams ll=lp(-1,1); ll.topMargin=16; ll.bottomMargin=14; task.addView(line,ll);
        task.addView(text("Stay connected for more Opportunities! Keep the tasks coming your way. ✅",16,ink,false));
        task.addView(text("▢  Published: 15 Oct 2026, 03:28 PM",13,muted,false)); card.addView(task);
        LinearLayout actions=row(); actions.setPadding(0,14,0,0);
        TextView likes=text("♡  "+(liked?104:103)+" Likes",15,muted,false); actions.addView(likes,new LinearLayout.LayoutParams(0,48,1));
        likeButton=button(liked?"♥ Liked":"👍",liked?0xFFFFDCDC:0xFFFFEEEE,0xFFE44848);
        likeButton.setOnClickListener(v->{ liked=!liked; given=liked?given+1:Math.max(0,given-1); likes.setText("♡  "+(liked?104:103)+" Likes"); likeButton.setText(liked?"♥ Liked":"👍"); statsGiven.setText("👍  "+given); breakText.setText(given+" / 150 likes"); });
        actions.addView(likeButton); card.addView(actions);
        content.addView(card);
        playButton=button(autoPlay?"Ⅱ  STOP AUTO PLAY":"▶  AUTO PLAY",navy,Color.WHITE);
        FrameLayout.LayoutParams fl=new FrameLayout.LayoutParams(-2,-2,Gravity.BOTTOM|Gravity.LEFT);
        fl.setMargins(18,0,0,20); frame.addView(playButton,fl);
        playButton.setOnClickListener(v->toggleAuto());
    }
    void toggleAuto() {
        autoPlay=!autoPlay;
        if(autoBadge!=null) autoBadge.setText(autoPlay?"● AutoPlay On":"● AutoPlay Idle");
        if(playButton!=null) playButton.setText(autoPlay?"Ⅱ  STOP AUTO PLAY":"▶  AUTO PLAY");
        Toast.makeText(this,autoPlay?"Demo Auto Play enabled":"Auto Play stopped",Toast.LENGTH_SHORT).show();
    }
    void pageMessage(String title,String msg) { new android.app.AlertDialog.Builder(this).setTitle(title).setMessage(msg).setPositiveButton("OK",null).show(); }
    void showDrawer() {
        if(drawerOpen) { showHome(); return; }
        drawerOpen=true;
        View dim=new View(this); dim.setBackgroundColor(0x77000000);
        frame.addView(dim,new FrameLayout.LayoutParams(-1,-1));
        LinearLayout drawer=column(); drawer.setBackgroundColor(0xFFF7F7FA); drawer.setPadding(22,28,22,20);
        FrameLayout.LayoutParams dp=new FrameLayout.LayoutParams((int)(getResources().getDisplayMetrics().widthPixels*0.84),-1,Gravity.LEFT);
        frame.addView(drawer,dp);
        LinearLayout profile=column(); profile.setPadding(0,24,0,28);
        profile.setBackground(shape(0xFFE4E6F4,0,20));
        TextView avatar=centerText("A",34,Color.WHITE); avatar.setTypeface(Typeface.DEFAULT,Typeface.BOLD); avatar.setBackground(shape(0xFF3749A8,0,100));
        LinearLayout.LayoutParams ap=lp(88,88); ap.setMargins(20,10,0,20); profile.addView(avatar,ap);
        TextView username=text("asik islam",24,Color.BLACK,true); pad(username,20,0,0,0); profile.addView(username);
        TextView follow=text("0 Followers     0 Following",15,muted,false); pad(follow,20,14,0,8); profile.addView(follow);
        drawer.addView(profile); space(drawer,24);
        String[] items={"▤   Feed","↪   My Links","◉   Campaign","◈   Explore","♟   Profile","⌁   Stats"};
        for(String item:items) {
            TextView nav=text(item,18,ink,true); nav.setPadding(18,15,12,15);
            nav.setBackground(shape(item.contains("Feed")?0xFFE2E5F1:0xFFF7F7FA,0,18));
            LinearLayout.LayoutParams np=lp(-1,58); np.bottomMargin=4; drawer.addView(nav,np);
            nav.setOnClickListener(v->{ if(item.contains("Feed")) showHome(); else pageMessage(item.replace("▤","").trim(),"This section is a UI demo. Connect your API to load real data."); });
        }
        View spacer=new View(this); drawer.addView(spacer,new LinearLayout.LayoutParams(1,0,1));
        TextView logout=button("⇥  Log Out",0xFFFBEAEA,0xFFE04C4C); drawer.addView(logout,lp(-1,58));
        logout.setOnClickListener(v->pageMessage("Log Out","Demo screen: no account is connected."));
        drawer.addView(centerText("Ad Network v1.0.19",13,0xFFB6B8C2),lp(-1,52));
        dim.setOnClickListener(v->showHome());
    }
}
