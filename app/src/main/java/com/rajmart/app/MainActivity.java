package com.rajmart.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.content.Intent;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.*;
import android.text.Editable;
import android.text.TextWatcher;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class MainActivity extends Activity {
    static class Product {
        String emoji, name, category, unit;
        int price;
        Product(String e, String n, String c, int p, String u) {
            emoji=e; name=n; category=c; price=p; unit=u;
        }
    }

    final int green = Color.rgb(20,110,65);
    final ArrayList<Product> catalog = new ArrayList<>();
    final LinkedHashMap<String,Integer> cartItems = new LinkedHashMap<>();
    LinearLayout productList;
    TextView cartButton, emptyLabel;
    EditText searchBox;
    Spinner categorySpinner;
    String selectedCategory = "All";

    @Override public void onCreate(Bundle state) {
        super.onCreate(state);
        getWindow().setStatusBarColor(green);
        getWindow().setNavigationBarColor(Color.BLACK);
        loadProducts();
        buildHome();
    }

    void loadProducts() {
        catalog.add(new Product("🍚","Basmati Rice","Grocery",120,"kg"));
        catalog.add(new Product("🥛","Fresh Milk","Dairy",60,"litre"));
        catalog.add(new Product("🍎","Fresh Apples","Fruits",140,"kg"));
        catalog.add(new Product("🥔","Potatoes","Vegetables",30,"kg"));
        catalog.add(new Product("🧼","Bath Soap","Personal Care",35,"piece"));
        catalog.add(new Product("🍌","Bananas","Fruits",50,"dozen"));
        catalog.add(new Product("🍅","Tomatoes","Vegetables",40,"kg"));
        catalog.add(new Product("🫖","Tea","Grocery",90,"pack"));
        catalog.add(new Product("🧈","Butter","Dairy",55,"pack"));
        catalog.add(new Product("🧴","Shampoo","Personal Care",120,"bottle"));
    }

    void buildHome() {
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(16,18,16,8);
        root.setBackgroundColor(Color.rgb(248,250,247));

        TextView brand = new TextView(this);
        brand.setText("🛒 Raj Mart");
        brand.setTextSize(28);
        brand.setTextColor(green);
        brand.setTypeface(null,1);
        root.addView(brand);

        TextView tagline = new TextView(this);
        tagline.setText("Your daily shopping partner");
        tagline.setTextSize(14);
        tagline.setTextColor(Color.DKGRAY);
        root.addView(tagline);

        cartButton = new TextView(this);
        cartButton.setTextSize(18);
        cartButton.setTextColor(Color.WHITE);
        cartButton.setGravity(Gravity.CENTER);
        cartButton.setPadding(12,14,12,14);
        cartButton.setBackgroundColor(green);
        cartButton.setOnClickListener(v -> showCart());
        LinearLayout.LayoutParams cartParams = new LinearLayout.LayoutParams(-1,-2);
        cartParams.setMargins(0,14,0,12);
        root.addView(cartButton,cartParams);
        updateCartLabel();

        searchBox = new EditText(this);
        searchBox.setSingleLine(true);
        searchBox.setTextSize(16);
        searchBox.setHint("Search products...");
        root.addView(searchBox,new LinearLayout.LayoutParams(-1,-2));

        categorySpinner = new Spinner(this);
        String[] categories = {"All","Grocery","Dairy","Fruits","Vegetables","Personal Care"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_dropdown_item,categories);
        categorySpinner.setAdapter(adapter);
        root.addView(categorySpinner);
        categorySpinner.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            public void onItemSelected(android.widget.AdapterView<?> p, View v, int pos, long id) {
                selectedCategory = categories[pos]; renderProducts();
            }
            public void onNothingSelected(android.widget.AdapterView<?> p) {}
        });
        searchBox.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s,int st,int count,int after) {}
            public void onTextChanged(CharSequence s,int st,int before,int count) { renderProducts(); }
            public void afterTextChanged(Editable e) {}
        });

        TextView heading = new TextView(this);
        heading.setText("Popular products");
        heading.setTextSize(21);
        heading.setTypeface(null,1);
        heading.setTextColor(Color.BLACK);
        heading.setPadding(0,10,0,6);
        root.addView(heading);

        ScrollView scroll = new ScrollView(this);
        productList = new LinearLayout(this);
        productList.setOrientation(LinearLayout.VERTICAL);
        scroll.addView(productList);
        root.addView(scroll,new LinearLayout.LayoutParams(-1,0,1));
        setContentView(root);
        renderProducts();
    }

    void renderProducts() {
        if (productList == null) return;
        productList.removeAllViews();
        String query = searchBox == null ? "" : searchBox.getText().toString().trim().toLowerCase();
        int shown = 0;
        for (Product p : catalog) {
            if (!selectedCategory.equals("All") && !p.category.equals(selectedCategory)) continue;
            if (!p.name.toLowerCase().contains(query)) continue;
            shown++;
            LinearLayout card = new LinearLayout(this);
            card.setGravity(Gravity.CENTER_VERTICAL);
            card.setPadding(12,12,8,12);
            card.setBackgroundColor(Color.WHITE);
            card.setElevation(2);
            LinearLayout.LayoutParams cp = new LinearLayout.LayoutParams(-1,-2);
            cp.setMargins(0,5,0,7);

            TextView icon = new TextView(this);
            icon.setText(p.emoji);
            icon.setTextSize(28);
            card.addView(icon);

            LinearLayout info = new LinearLayout(this);
            info.setOrientation(LinearLayout.VERTICAL);
            info.setPadding(12,0,4,0);
            TextView title = new TextView(this);
            title.setText(p.name);
            title.setTextSize(17);
            title.setTextColor(Color.BLACK);
            title.setTypeface(null,1);
            info.addView(title);
            TextView price = new TextView(this);
            price.setText("₹"+p.price+" / "+p.unit);
            price.setTextSize(15);
            price.setTextColor(Color.DKGRAY);
            info.addView(price);
            card.addView(info,new LinearLayout.LayoutParams(0,-2,1));

            Button add = new Button(this);
            add.setText("ADD");
            add.setOnClickListener(v -> {
                int qty = cartItems.containsKey(p.name) ? cartItems.get(p.name) : 0;
                cartItems.put(p.name,qty+1);
                updateCartLabel();
                Toast.makeText(this,p.name+" added",Toast.LENGTH_SHORT).show();
            });
            card.addView(add);
            productList.addView(card,cp);
        }
        if (shown == 0) {
            TextView none = new TextView(this);
            none.setText("No products found");
            none.setTextSize(16);
            none.setPadding(8,24,8,24);
            productList.addView(none);
        }
    }

    int itemCount() {
        int count=0;
        for (int q : cartItems.values()) count += q;
        return count;
    }

    int cartTotal() {
        int total=0;
        for (Product p : catalog) {
            Integer q=cartItems.get(p.name);
            if (q!=null) total += p.price*q;
        }
        return total;
    }

    void updateCartLabel() {
        if (cartButton!=null) cartButton.setText("🧺  View Cart  •  "+itemCount()+" items  •  ₹"+cartTotal());
    }

    void showCart() {
        LinearLayout box = new LinearLayout(this);
        box.setOrientation(LinearLayout.VERTICAL);
        box.setPadding(18,8,18,8);
        ScrollView scroll = new ScrollView(this);
        LinearLayout rows = new LinearLayout(this);
        rows.setOrientation(LinearLayout.VERTICAL);
        scroll.addView(rows);
        box.addView(scroll,new LinearLayout.LayoutParams(-1,0,1));

        if (cartItems.isEmpty()) {
            TextView empty = new TextView(this);
            empty.setText("Your cart is empty. Add some products first.");
            empty.setTextSize(16);
            rows.addView(empty);
        } else {
            for (Product p : catalog) {
                Integer quantity=cartItems.get(p.name);
                if (quantity==null || quantity==0) continue;
                LinearLayout row = new LinearLayout(this);
                row.setGravity(Gravity.CENTER_VERTICAL);
                TextView name = new TextView(this);
                name.setText(p.emoji+" "+p.name+"\n₹"+p.price+" × "+quantity);
                name.setTextSize(15);
                name.setTextColor(Color.BLACK);
                row.addView(name,new LinearLayout.LayoutParams(0,-2,1));
                Button minus = new Button(this); minus.setText("−");
                minus.setOnClickListener(v -> {
                    int q=cartItems.get(p.name)-1;
                    if(q<=0) cartItems.remove(p.name); else cartItems.put(p.name,q);
                    updateCartLabel(); showCart();
                });
                row.addView(minus);
                Button plus = new Button(this); plus.setText("+");
                plus.setOnClickListener(v -> {
                    cartItems.put(p.name,cartItems.get(p.name)+1);
                    updateCartLabel(); showCart();
                });
                row.addView(plus);
                rows.addView(row);
            }
        }
        TextView total = new TextView(this);
        total.setText("Total: ₹"+cartTotal());
        total.setTextSize(20);
        total.setTypeface(null,1);
        total.setPadding(0,12,0,8);
        box.addView(total);

        AlertDialog dialog = new AlertDialog.Builder(this)
            .setTitle("Your Cart")
            .setView(box)
            .setNegativeButton("Continue Shopping",(d,w)->{})
            .setPositiveButton("Checkout",(d,w)-> {
                if (!cartItems.isEmpty()) showCheckout();
                else Toast.makeText(this,"Your cart is empty",Toast.LENGTH_SHORT).show();
            }).create();
        dialog.show();
    }

    void showCheckout() {
        LinearLayout form = new LinearLayout(this);
        form.setOrientation(LinearLayout.VERTICAL);
        form.setPadding(20,8,20,4);
        EditText customer = new EditText(this);
        customer.setHint("Customer name");
        form.addView(customer);
        EditText phone = new EditText(this);
        phone.setHint("Mobile number");
        phone.setInputType(3);
        form.addView(phone);
        EditText address = new EditText(this);
        address.setHint("Delivery address");
        address.setMinLines(2);
        address.setGravity(Gravity.TOP);
        form.addView(address);

        new AlertDialog.Builder(this)
            .setTitle("Delivery Details")
            .setView(form)
            .setNegativeButton("Cancel",null)
            .setPositiveButton("Send Order",(d,w)-> {
                if (customer.getText().toString().trim().isEmpty() ||
                    phone.getText().toString().trim().isEmpty() ||
                    address.getText().toString().trim().isEmpty()) {
                    Toast.makeText(this,"Please enter name, mobile and address",Toast.LENGTH_LONG).show();
                    return;
                }
                shareOrder(customer.getText().toString().trim(),phone.getText().toString().trim(),address.getText().toString().trim());
            }).show();
    }

    void shareOrder(String customer,String phone,String address) {
        StringBuilder message = new StringBuilder();
        message.append("RAJ MART ORDER\n");
        message.append("Name: ").append(customer).append("\nMobile: ").append(phone);
        message.append("\nAddress: ").append(address).append("\n\nItems:\n");
        for (Product p : catalog) {
            Integer q=cartItems.get(p.name);
            if(q!=null && q>0) message.append(p.name).append(" x ").append(q).append(" = ₹").append(p.price*q).append("\n");
        }
        message.append("\nTotal: ₹").append(cartTotal());
        message.append("\nPlease confirm availability and delivery.");
        Intent send = new Intent(Intent.ACTION_SEND);
        send.setType("text/plain");
        send.putExtra(Intent.EXTRA_SUBJECT,"Raj Mart Order");
        send.putExtra(Intent.EXTRA_TEXT,message.toString());
        startActivity(Intent.createChooser(send,"Send order with"));
    }
}
