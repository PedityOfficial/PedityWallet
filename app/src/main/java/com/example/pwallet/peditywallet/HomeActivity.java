package com.example.pwallet.peditywallet;

import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.stellar.sdk.KeyPair;
import org.stellar.sdk.Network;
import org.stellar.sdk.Server;
import org.stellar.sdk.responses.AccountResponse;


public class HomeActivity extends Fragment {
    private TextView viewbalance;
    private TextView accountId;
    private String bal = "";
    private String account;
    private View view;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            View view = inflater.inflate(R.layout.content_main, container, false);

            view.findViewById(R.id.balance_table).setVisibility(View.GONE);

            Async1 mAuthTask = new Async1();
            mAuthTask.execute((Void) null);

            return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (bal != "") {
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    class Async1 extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {

            try {
                String secretKey = (new Decryptor()).decryptData(getContext(), "SecretKeyAlias_PEDITY6");
                Network.usePublicNetwork();
                KeyPair keyPair = KeyPair.fromSecretSeed(secretKey);

                Server server = new Server("http://horizon.stellar.org");
                AccountResponse accountResponse = server.accounts().account(keyPair);

                for(AccountResponse.Balance balance : accountResponse.getBalances()) {
                    if(balance.getAssetType().equals("native")) {
                        final String newbal = balance.getBalance();
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                TextView xlmbalance= getActivity().findViewById(R.id.xlmbalance);
                                xlmbalance.setText(newbal);
                            }
                        });
                    }
                    else if(balance.getAssetType().equals("credit_alphanum4")) {
                        KeyPair issuer = balance.getAssetIssuer();
                            bal = balance.getBalance();
                            final String assetcode = balance.getAssetCode();
                            final String newbal = bal;
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                        public void run() {
                                TableRow tbrow = new TableRow(getActivity());
                                    tbrow.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.ic_table_cell));
                                    float scale = getResources().getDisplayMetrics().density;
                                    int dpAsPixels = (int) (10*scale + 0.5f);
                                    tbrow.setPadding(dpAsPixels, dpAsPixels, 5, dpAsPixels);
                                tbrow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                                TableRow.LayoutParams lp1 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
                                TextView t1v = new TextView(getActivity());
                                t1v.setTextSize(20);
                                t1v.setGravity(Gravity.LEFT);
                                t1v.setTypeface(null, Typeface.BOLD);
                                t1v.setText(assetcode);
                                t1v.setLayoutParams(lp1);
                                tbrow.addView(t1v);
                                TextView t2v = new TextView(getActivity());
                                t2v.setTextSize(20);
                                t2v.setLayoutParams(lp1);
                                t2v.setGravity(Gravity.LEFT);
                                t2v.setText(newbal);
                                tbrow.addView(t2v);
                                TableLayout ll = getView().findViewById(R.id.balance_table);
                                ll.addView(tbrow);
                                }
                            });

                        //}
                    }
                }
            }
            catch (Exception e) {
                return false;
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if(result) {
                getView().findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                getView().findViewById(R.id.balance_table).setVisibility(View.VISIBLE);
            }
        }

    }
}
