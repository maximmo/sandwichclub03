package ru.serv_techno.sandwichclub03.fragments;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.serv_techno.sandwichclub03.APIv1;
import ru.serv_techno.sandwichclub03.ApiFactory;
import ru.serv_techno.sandwichclub03.models.MyOrder;
import ru.serv_techno.sandwichclub03.R;
import ru.serv_techno.sandwichclub03.RecyclerClickListener;
import ru.serv_techno.sandwichclub03.adapters.OrderListAdapter;

/**
 * Created by Maxim on 02.06.2017.
 */

public class OrderListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    SwipeRefreshLayout swipe_container;
    RecyclerView RecyclerViewOrderList;
    OnOrderSelectedListener mCallback;

    public interface OnOrderSelectedListener{
        void onOrderSelected(MyOrder myOrder);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.order_list_fragment, null);

        swipe_container = (SwipeRefreshLayout)rootview.findViewById(R.id.swipe_container);
        swipe_container.setOnRefreshListener(this);
        swipe_container.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        RecyclerViewOrderList  = (RecyclerView) rootview.findViewById(R.id.OrderListRecyclerView);
        RecyclerViewOrderList.setLayoutManager(new LinearLayoutManager(getActivity()));

        List<MyOrder> orderList = GetOrderList();
        InitAdapter(orderList);

        return rootview;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public List<MyOrder> GetOrderList(){
        List<MyOrder> orderList = MyOrder.listAll(MyOrder.class);
        Collections.sort(orderList, new Comparator<MyOrder>() {
            @Override
            public int compare(MyOrder lhs, MyOrder rhs) {
                return Integer.parseInt(rhs.getId().toString()) - Integer.parseInt(lhs.getId().toString());
            }
        });

        return orderList;
    }

    public void InitAdapter(List<MyOrder> orderList){
        final OrderListAdapter orderListAdapter = new OrderListAdapter(getActivity(), orderList);
        RecyclerViewOrderList.setAdapter(orderListAdapter);
        orderListAdapter.notifyDataSetChanged();
        RecyclerViewOrderList.setItemAnimator(new DefaultItemAnimator());
        RecyclerViewOrderList.addOnItemTouchListener(new RecyclerClickListener(getActivity()) {
            @Override
            public void onItemClick(RecyclerView recyclerView, View itemView, int position) {
                //здесь вернем выбранный заказ в активити
                //далее активити проинициализирует фрагмент с деталями заказа
                MyOrder order = orderListAdapter.getItem(position);
                mCallback.onOrderSelected(order);
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
                return;
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try{
            mCallback = (OnOrderSelectedListener) context;
        }catch (ClassCastException e){
            throw new ClassCastException(context.toString()
                    + " must implement OnOrderSelectedListener");
        }
    }

    @Override
    public void onRefresh(){
        swipe_container.setRefreshing(true);
        UpdateNewOrdersStatus updateNewOrdersStatus = new UpdateNewOrdersStatus();
        updateNewOrdersStatus.execute(RecyclerViewOrderList.getAdapter());
    }

    class UpdateNewOrdersStatus extends AsyncTask<RecyclerView.Adapter, Void, Void> {

        @Override
        protected void onPreExecute() {
            swipe_container.setRefreshing(true);
        }

        @Override
        protected Void doInBackground(RecyclerView.Adapter... params) {

            final String LOG_TAG = "snoopy_st_log";
            List<MyOrder> orderList = MyOrder.getOrdersByStatus("new");

            for(final MyOrder order : orderList){
                String filter = "" + "{'id':" + String.valueOf(order.extid) + "}";
                try {
                    retrofit2.Response<ResponseBody> response = ApiFactory.getInstance().getApi().getOrder(filter).execute();
                    if(response.code()==200||response.code()==201){
                        ResponseBody responseBody = response.body();
                        String MyMessage = responseBody.string();

                        JSONArray jsonArray = new JSONArray(MyMessage);

                        if(jsonArray.length()>0){
                            JSONObject orderJson = jsonArray.getJSONObject(0);
                            order.status = orderJson.getString("status");
                            order.save();
                        }
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e(LOG_TAG, e.getMessage());
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e(LOG_TAG, e.getMessage());
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            List<MyOrder> orderList = GetOrderList();
            InitAdapter(orderList);
            swipe_container.setRefreshing(false);
        }

        @Override
        protected void onCancelled() {
            RecyclerViewOrderList.invalidate();
            swipe_container.setRefreshing(false);
        }
    }


}
