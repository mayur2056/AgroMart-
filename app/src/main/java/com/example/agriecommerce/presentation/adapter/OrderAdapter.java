//package com.example.agriecommerce.presentation.adapter;
//
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.example.agriecommerce.R;
//import com.example.agriecommerce.model.Order;
//
//import java.util.List;
//
//public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {
//    private List<Order> orderList;
//
//    public OrderAdapter(List<Order> orderList) {
//        this.orderList = orderList;
//    }
//
//    @NonNull
//    @Override
//    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false);
//        return new OrderViewHolder(itemView);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
//        Order order = orderList.get(position);
//        holder.orderName.setText(order.getOrderId());
//        holder.orderPrice.setText("Price: " + order.getTotalAmount());
//        holder.orderDate.setText("Date: " + order.getOrderDate());
//    }
//
//    @Override
//    public int getItemCount() {
//        return orderList.size();
//    }
//
//    public static class OrderViewHolder extends RecyclerView.ViewHolder {
//        TextView orderName, orderPrice, orderDate;
//
//        public OrderViewHolder(View itemView) {
//            super(itemView);
//            orderName = itemView.findViewById(R.id.orderName);
//            orderPrice = itemView.findViewById(R.id.orderPrice);
//            orderDate = itemView.findViewById(R.id.orderDate);
//        }
//    }
//}
