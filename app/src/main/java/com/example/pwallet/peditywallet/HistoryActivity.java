package com.example.pwallet.peditywallet;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;


import org.stellar.sdk.Asset;
import org.stellar.sdk.AssetTypeCreditAlphaNum;
import org.stellar.sdk.AssetTypeNative;
import org.stellar.sdk.KeyPair;
import org.stellar.sdk.Network;
import org.stellar.sdk.Server;
import org.stellar.sdk.requests.EventListener;
import org.stellar.sdk.requests.PaymentsRequestBuilder;
import org.stellar.sdk.requests.RequestBuilder;
import org.stellar.sdk.responses.operations.OperationResponse;
import org.stellar.sdk.responses.operations.PaymentOperationResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    ProgressBar mProgressbar;
    List<AccountData> accountDataList;
    String typeid;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        mRecyclerView = view.findViewById(R.id.history_recycler_view);
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this.getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        accountDataList = new ArrayList<>();
        mAdapter = new MyAdapter(getContext(),accountDataList);
        mRecyclerView.setAdapter(mAdapter);

        mProgressbar = view.findViewById(R.id.progress_bar);

        Async3 mHistoryTask = new Async3();
        mHistoryTask.execute((Void) null);

        return view;
    }

    class Async3 extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            mRecyclerView.setVisibility(View.GONE);
            mProgressbar.setVisibility(View.VISIBLE);
        }
        @Override
        protected Boolean doInBackground(Void... params) {

            try {
                String secretKey = (new Decryptor()).decryptData(getContext(), "SecretKeyAlias_PEDITY6");
                final String accid;
                Network.usePublicNetwork();
                Server server = new Server("https://horizon.stellar.org");

                KeyPair keyPair = KeyPair.fromSecretSeed(secretKey);

                accid = keyPair.getAccountId();

                final KeyPair account = KeyPair.fromAccountId(accid);

                PaymentsRequestBuilder paymentsRequest = server.payments().forAccount(account);

                paymentsRequest.order(RequestBuilder.Order.DESC);

                paymentsRequest.stream(new EventListener<OperationResponse>() {
                    @Override
                    public void onEvent(OperationResponse payment) {

                        if (payment instanceof PaymentOperationResponse) {

                            String amount = ((PaymentOperationResponse) payment).getAmount();

                            Asset asset = ((PaymentOperationResponse) payment).getAsset();
                            String assetName;
                            if (asset.equals(new AssetTypeNative())) {
                                assetName = "lumens";
                            } else {
                                StringBuilder assetNameBuilder = new StringBuilder();
                                assetNameBuilder.append(((AssetTypeCreditAlphaNum) asset).getCode());
                                assetNameBuilder.append(":");
                                assetNameBuilder.append(((AssetTypeCreditAlphaNum) asset).getIssuer().getAccountId());
                                assetName = assetNameBuilder.toString();

                            if (((PaymentOperationResponse) payment).getTo().getAccountId().equals(accid)) {
                                typeid ="Received";
                                AccountData data = new AccountData( amount,((PaymentOperationResponse) payment).getFrom().getAccountId(),typeid);
                                accountDataList.add(data);
                            }
                            else if (((PaymentOperationResponse) payment).getFrom().getAccountId().equals(accid)) {
                                typeid = "Sent";
                                AccountData data = new AccountData( amount,((PaymentOperationResponse) payment).getTo().getAccountId(),typeid);
                                accountDataList.add(data);
                            }

                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mAdapter.notifyDataSetChanged();
                                }
                            });
                            }
                        }
                    }
                });

                try {
                    System.in.read();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            catch (Exception e) {
                return false;
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (result) {
                mAdapter.notifyDataSetChanged();
                mRecyclerView.setVisibility(View.VISIBLE);
                mProgressbar.setVisibility(View.GONE);
            }
        }

    }

}
