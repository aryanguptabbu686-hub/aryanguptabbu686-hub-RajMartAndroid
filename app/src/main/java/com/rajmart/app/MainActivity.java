package com.rajmart.app;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.*;

public class MainActivity extends Activity {
    int cartCount = 0;
    TextView cart;
    LinearLayout products;

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(Color.rgb(20, 110, 65));
        getWindow().setNavigationBarColor(Color.BLACK);
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(24, 24, 24, 24);
        root.setBackgroundColor(Color.rgb(248, 250, 247));

        TextView brand = new TextView(this);
        brand.setText("🛒 Raj Mart");
        brand.setTextSize(28);
        brand.setTextColor(Color.rgb(20, 110, 65));
        brand.setTypeface(null, 1);
        root.addView(brand);

        TextView tagline = new TextView(this);
        tagline.setText("Your daily shopping partner");
        tagline.setTextSize(14);
        tagline.setTextColor(Color.DKGRAY);
        root.addView(tagline);

        cart = new TextView(this);
        cart.setText("🧺 Cart: 0 items");
        cart.setTextSize(18);
        cart.setPadding(0, 22, 0, 18);
        cart.setTextColor(Color.BLACK);
        root.addView(cart);

        TextView heading = new TextView(this);
        heading.setText("Popular products");
        heading.setTextSize(22);
        heading.setTypeface(null, 1);
        heading.setTextColor(Color.BLACK);
        root.addView(heading);

        ScrollView scroll = new ScrollView(this);
        products = new LinearLayout(this);
        products.setOrientation(LinearLayout.VERTICAL);
        scroll.addView(products);
        root.addView(scroll, new LinearLayout.LayoutParams(-1, 0, 1));

        addProduct("🍚", "Basmati Rice", "₹120 / kg");
        addProduct("🥛", "Fresh Milk", "₹out 60 / litre".replace("out ", ""));
        addProduct("🍎", "Fresh Apples", "₹140 / kg");
        addProduct("🥔", "Potatoes", "₹30 / kg");
        addProduct("🧼", "Bath Soap", "₹35 / piece");

        setContentView(root);
    }

    void addProduct(String emoji, String name, String price) {
        LinearLayout card = new LinearLayout(this);
        card.setGravity(Gravity.CENTER_VERTICAL);
        card.setPadding(16, 18, 16, 18);
        card.setBackgroundColor(Color.WHITE);
        LinearLayout.LayoutParams cp = new LinearLayout.LayoutParams(-1, -2);
        cp.setMargins(0, 14, 0, 0);
        card.setElevation(3);
        TextView icon = new TextView(this);
        icon.setText(emoji);
        icon.setTextSize(30);
        card.addView(icon);
        LinearLayout info = new LinearLayout(this);
        info.setOrientation(LinearLayout.VERTICAL);
        info.setPadding(16, 0, 8, 0);
        TextView title = new TextView(this);
        title.setText(name);
        title.setTextSize(17);
        title.setTextColor(Color.BLACK);
        title.setTypeface(null, 1);
        info.addView(title);
        TextView cost = new TextView(this);
        cost.setText(price);
        cost.setTextSize(15);
        cost.setTextColor(Color.DKGRAY);
        info.addView(cost);
        card.addView(info, new LinearLayout.LayoutParams(0, -2, 1));
        Button add = new Button(this);
        add.setText("Add");
        add.setOnClickListener(v -> {
            cartCount++;
            cart.setText("🧺 Cart: " + cartCount + " items");
            Toast.makeText(this, name + " added to cart", Toast.LENGTH_SHORT).show();
        });
        card.addView(add);
        products.addView(card, cp);
    }
}
