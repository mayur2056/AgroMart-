package com.example.agriecommerce.presentation.fragment;

import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.Manifest;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieDrawable;
//import com.example.agriecommerce.Manifest;
import com.example.agriecommerce.R;
import com.example.agriecommerce.model.CartItem;
import com.example.agriecommerce.model.Order;
import com.example.agriecommerce.presentation.activity.SendEmailTask;
import com.example.agriecommerce.presentation.adapter.CartAdapter;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class BagFragment extends Fragment  {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;
    private FusedLocationProviderClient fusedLocationClient;

    private RecyclerView cartRecView;
    private CartAdapter cartAdapter;
    private LottieAnimationView animationView;
    private TextView totalPriceBagFrag;
    private LinearLayout bottomCartLayout, emptyBagMsgLayout;
    private Button checkOutButton;
    private List<CartItem> cartItems;
    private DatabaseReference cartRef, ordersRef;
    private FirebaseUser currentUser;
    EditText editCity;
    Button btnLocation;
    RadioGroup paymentRbg;
    RadioButton cod,online;
    private int totalAmount = 0;

    double latitude,longitude;
    String str;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bag, container, false);

        cartRecView = view.findViewById(R.id.cartRecView);
        animationView = view.findViewById(R.id.animationViewCartPage);
        totalPriceBagFrag = view.findViewById(R.id.totalPriceBagFrag);
        bottomCartLayout = view.findViewById(R.id.bottomCartLayout);
        emptyBagMsgLayout = view.findViewById(R.id.emptyBagMsgLayout);
        checkOutButton = view.findViewById(R.id.checkOut_BagPage);
        editCity=view.findViewById(R.id.editCity);
        btnLocation=view.findViewById(R.id.btnLocation);
        paymentRbg=view.findViewById(R.id.paymentRbg);
        cod=view.findViewById(R.id.cod);
        online=view.findViewById(R.id.online);

        cartItems = new ArrayList<>();

        cartAdapter = new CartAdapter(getContext(), cartItems, new CartAdapter.OnCartItemClickListener() {
            @Override
            public void onQuantityIncrease(CartItem item) {
                item.setQuantity(item.getQuantity() + 1);
                updateCartItemInDatabase(item);
            }

            @Override
            public void onQuantityDecrease(CartItem item) {
                if (item.getQuantity() > 1) {
                    item.setQuantity(item.getQuantity() - 1);
                    updateCartItemInDatabase(item);
                }
            }

            @Override
            public void onRemoveItem(CartItem item) {
                removeCartItemFromDatabase(item);
            }
        });

        cartRecView.setLayoutManager(new LinearLayoutManager(getContext()));
        cartRecView.setAdapter(cartAdapter);

        animationView.playAnimation();
        animationView.setRepeatCount(LottieDrawable.INFINITE);
        bottomCartLayout.setVisibility(View.GONE);
        emptyBagMsgLayout.setVisibility(View.VISIBLE);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userEmail = currentUser.getEmail();
            if (userEmail != null) {
                String sanitizedEmail = userEmail.replace(".", "_").replace("@", "_");
                cartRef = FirebaseDatabase.getInstance().getReference("Cart").child(sanitizedEmail);
                ordersRef = FirebaseDatabase.getInstance().getReference("Orders").child(sanitizedEmail);
                loadCartData();
            }
        }

        checkOutButton.setOnClickListener(v -> startPayment());

        btnLocation.setOnClickListener(v -> getLocation());

        return view;
    }

    public void getLocation() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            retrieveLocation();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                retrieveLocation();
            } else {
                Toast.makeText(getContext(), "Location permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void retrieveLocation() {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(requireActivity(), new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                             latitude = location.getLatitude();
                             longitude = location.getLongitude();
                            Log.d("Location", "Latitude: " + latitude + ", Longitude: " + longitude);
                            Toast.makeText(getContext(), "Latitude: " + latitude + ", Longitude: " + longitude, Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getContext(), "Unable to retrieve location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void loadCartData() {
        cartRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<CartItem> newCartList = new ArrayList<>();
                totalAmount = 0;

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    CartItem cartItem = dataSnapshot.getValue(CartItem.class);
                    if (cartItem != null) {
                        newCartList.add(cartItem);
                        int sum = Integer.parseInt(cartItem.getPrice()) * cartItem.getQuantity();
                        totalAmount += sum;
                    }
                }

                // Ensure cartItems list is updated with the new data
                cartItems.clear();
                cartItems.addAll(newCartList);
                // Handle the UI changes based on cart data
                if (newCartList.isEmpty()) {
                    animationView.playAnimation();
                    bottomCartLayout.setVisibility(View.GONE);
                    emptyBagMsgLayout.setVisibility(View.VISIBLE);
                    cartRecView.setVisibility(View.GONE);
                } else {
                    animationView.pauseAnimation();
                    bottomCartLayout.setVisibility(View.VISIBLE);
                    emptyBagMsgLayout.setVisibility(View.GONE);
                    cartRecView.setVisibility(View.VISIBLE);
                }

                totalPriceBagFrag.setText("Rs " + totalAmount);
                cartAdapter.updateList(newCartList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Failed to load cart: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void startPayment() {
        int id=paymentRbg.getCheckedRadioButtonId();
         str="";
        if(id!=-1){
            RadioButton select= requireView().findViewById(id);
            str=select.getText().toString();
            Log.d("Radio Button:",str);

        }
        if (totalAmount <= 0) {
            Toast.makeText(getContext(), "Invalid Amount!", Toast.LENGTH_SHORT).show();
            return;
        }

        if(str.equals("Pay Online")) {

            Checkout checkout = new Checkout();
            checkout.setKeyID("rzp_test_HYCG9XE3B6gozQ");  // Use your actual Razorpay API Key
            checkout.setImage(R.drawable.logo);  // Optional: Add your app logo

            try {
                JSONObject options = new JSONObject();
                options.put("name", "Agri E-Commerce");
                options.put("description", "Order Payment");
                options.put("currency", "INR");
                options.put("amount", totalAmount * 100);  // Convert to paisa (100 INR = 10000 paisa)
                options.put("prefill.email", currentUser.getEmail());
                options.put("prefill.contact", "9876543210"); // Change with actual phone number if available

                checkout.open(requireActivity(), options);
//            FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
//            transaction.replace(R.id.nav_fragment, new BagFragment());
//            transaction.addToBackStack(null);
//            transaction.commit();
                saveOrderToDatabase();
            } catch (Exception e) {
                Toast.makeText(getContext(), "Payment Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                Log.e("RazorpayError", "Error starting Razorpay: ", e);
            }
        } else{
            saveOrderToDatabase();
        }
    }



    public void handlePaymentSuccess(String razorpayPaymentID) {
        Log.d("BagFragment", "Payment Successful! ID: " + razorpayPaymentID);
        Toast.makeText(getContext(), "Payment Successful!", Toast.LENGTH_SHORT).show();

        // Save order after successful payment
//        saveOrderToDatabase(razorpayPaymentID);
    }

    public void handlePaymentError(int code, String response) {
        Log.e("BagFragment", "Payment Failed. Code: " + code + " | Response: " + response);

        String errorMessage;
        switch (code) {
            case Checkout.NETWORK_ERROR:
                errorMessage = "Network error! Check your internet connection.";
                break;
            case Checkout.INVALID_OPTIONS:
                errorMessage = "Invalid payment details. Contact support.";
                break;
            case Checkout.PAYMENT_CANCELED:
                errorMessage = "Payment was cancelled by the user.";
                break;
            case Checkout.TLS_ERROR:
                errorMessage = "Security issue. Please update your device.";
                break;
            default:
                errorMessage = "Transaction failed! Please try again.";
                break;
        }

        Toast.makeText(getContext(), errorMessage, Toast.LENGTH_LONG).show();
    }


    private void saveOrderToDatabase() {
        if (ordersRef == null || cartItems.isEmpty()) {
            Toast.makeText(getContext(), "Failed to place order. Cart is empty!", Toast.LENGTH_SHORT).show();
            return;
        }

        String orderID = ordersRef.push().getKey();
        if (orderID == null) return;

        String userEmail = currentUser.getEmail();

        // Get current date and time
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        String orderDate = dateFormat.format(new Date());
        String orderTime = timeFormat.format(new Date());

        // Prepare order list
        List<Map<String, Object>> orderedProducts = new ArrayList<>();

        for (CartItem cartItem : cartItems) {
            Map<String, Object> productData = new HashMap<>();
            productData.put("productName", cartItem.getName());
            productData.put("productPrice", cartItem.getPrice());
            productData.put("productImage", cartItem.getImageUrl());  // Assuming imageUrl is in CartItem
            productData.put("quantity", cartItem.getQuantity());
            productData.put("farmName", cartItem.getFarmName());
            productData.put("status", "Shipped");  // Default status



            orderedProducts.add(productData);
        }

        // Create order data
        Map<String, Object> orderData = new HashMap<>();
        orderData.put("orderID", orderID);
        orderData.put("userEmail", userEmail);
        orderData.put("totalAmount", totalAmount);
        orderData.put("orderDate", orderDate);
        orderData.put("orderTime", orderTime);
        orderData.put("products", orderedProducts);
        orderData.put("latitude",latitude);
        orderData.put("longitude",longitude);
        orderData.put("city",editCity.getText().toString());
        orderData.put("paymentMethod",str);

        // Save order to Firebase
        ordersRef.child(orderID).setValue(orderData)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(getContext(), "Order placed successfully!", Toast.LENGTH_SHORT).show();
                    updateProductStock(orderID);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Order placement failed!", Toast.LENGTH_SHORT).show();
                });
    }

    private void updateProductStock(String orderID) {
        DatabaseReference productRef = FirebaseDatabase.getInstance().getReference("products");

        // 🛠 Debugging
        Log.d("StockUpdate", "Cart Items Size: " + cartItems.size());

        if (cartItems == null || cartItems.isEmpty()) {
            Log.e("StockUpdate", "cartItems is empty, skipping stock update.");
            return;
        }

        for (CartItem cartItem : cartItems) {
            // Fetch product name and farm name
            String productName = cartItem.getName();
            String farmName = cartItem.getFarmName();

            if (productName == null || farmName == null || productName.isEmpty() || farmName.isEmpty()) {
                Log.e("StockUpdate", "Product name or farm name is missing for cart item: " + cartItem);
                continue; // Skip if product or farm name is missing
            }

            Log.d("StockUpdate", "Updating Product: " + productName + " from Farm: " + farmName + " | Purchased Quantity: " + cartItem.getQuantity());

            int purchasedQuantity = cartItem.getQuantity();

            // Query products by product name and farm name
            productRef.orderByChild("name").equalTo(productName).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    boolean productFound = false;
                    for (DataSnapshot productSnapshot : snapshot.getChildren()) {
                        String productFarmName = productSnapshot.child("farmName").getValue(String.class);

                        // Match farm name as well
                        if (productFarmName != null && productFarmName.equals(farmName)) {
                            productFound = true;
                            Integer currentQuantity = productSnapshot.child("quantity").getValue(Integer.class);
                            Log.d("StockUpdate", "Current Stock for " + productName + " = " + currentQuantity);

                            if (currentQuantity != null) {
                                int newQuantity = Math.max(0, currentQuantity - purchasedQuantity);
                                boolean isStock = newQuantity > 0;

                                // Update stock in Firebase
                                productSnapshot.getRef().child("quantity").setValue(newQuantity);
                                productSnapshot.getRef().child("isStock").setValue(isStock);
                                String emailBody = "Dear Customer,\n\nYour order has been placed successfully!\nOrder ID: " + orderID +
                                        "\nTotal Amount: Rs. " + totalAmount +
                                        "\n\nThank you for shopping with us!\n\nBest Regards,\nAgri E-Commerce Team";

                                new SendEmailTask(currentUser.getEmail(), "Order Confirmation", emailBody).execute();
                                Log.d("StockUpdate", "Updated Stock for " + productName + " = " + newQuantity);

                                // Clear cart after updating stock
                                if (cartItems.indexOf(cartItem) == cartItems.size() - 1) {
                                    clearCart();
//
                                }
                            }
                        }
                    }

                    if (!productFound) {
                        Log.e("StockUpdate", "Product with name " + productName + " and farm " + farmName + " not found in database!");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e("StockUpdate", "Failed to fetch product data: " + error.getMessage());
                }
            });
        }
    }
    private void clearCart() {
        cartRef.removeValue()
                .addOnSuccessListener(aVoid -> {
                    // Ensure the fragment is still attached before performing UI operations
                    if (getActivity() != null && isAdded()) {
                        Toast.makeText(getActivity(), "Cart Cleared!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    // Log the error and show a message, but ensure fragment is attached
                    if (getActivity() != null && isAdded()) {
                        Log.e("FirebaseError", "Failed to clear cart: " + e.getMessage());
                        Toast.makeText(getActivity(), "Failed to clear cart!", Toast.LENGTH_SHORT).show();
                    }
                });
    }




    private void updateCartItemInDatabase(CartItem item) {
        cartRef.child(item.getCartId()).setValue(item);
    }

    private void removeCartItemFromDatabase(CartItem item) {
        cartRef.child(item.getCartId()).removeValue();
    }
////





}
