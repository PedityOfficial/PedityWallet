package com.example.pwallet.peditywallet;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

            view.findViewById(R.id.balance_amount).setVisibility(View.GONE);
            view.findViewById(R.id.balance_native).setVisibility(View.GONE);

            Async1 mAuthTask = new Async1();
            mAuthTask.execute((Void) null);

            return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (bal != "") {
            viewbalance = getActivity().findViewById(R.id.balance_amount);
            viewbalance.setText(bal);
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
                    if(balance.getAssetType().equals("credit_alphanum4")) {
                        KeyPair issuer = balance.getAssetIssuer();
                        if (balance.getAssetCode().equals("PEDI") && issuer.getAccountId().equals("GBVUDZLMHTLMZANLZB6P4S4RYF52MVWTYVYXTQ2EJBPBX4DZI2SDOLLY")) {
                            bal = balance.getBalance();
                            final String newbal = bal + " PEDI";

                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                        public void run() {
                                             viewbalance = getView().findViewById(R.id.balance_amount);
                                             viewbalance.setText(newbal);
                                }
                            });

                        }
                    }
                    else {
                        bal = balance.getBalance();
                        final String newbal = bal + " XLM";
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                viewbalance = getView().findViewById(R.id.balance_native);
                                viewbalance.setText(newbal);
                            }
                        });
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
                getView().findViewById(R.id.balance_amount).setVisibility(View.VISIBLE);
                getView().findViewById(R.id.balance_native).setVisibility(View.VISIBLE);
            }
        }

    }
}
