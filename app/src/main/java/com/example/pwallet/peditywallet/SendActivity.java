package com.example.pwallet.peditywallet;


import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;


import org.stellar.sdk.Asset;
import org.stellar.sdk.AssetTypeNative;
import org.stellar.sdk.KeyPair;
import org.stellar.sdk.Memo;
import org.stellar.sdk.Network;
import org.stellar.sdk.PaymentOperation;
import org.stellar.sdk.Server;
import org.stellar.sdk.Transaction;
import org.stellar.sdk.responses.AccountResponse;
import org.stellar.sdk.responses.SubmitTransactionResponse;

public class SendActivity extends Fragment implements View.OnClickListener {

    private EditText dest_acc_id;
    private EditText send_amt;
    private EditText memo_txt;
    private CheckBox chkbox_btn;
    private Button send_btn;
    private Spinner spinner;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.activity_send, container, false);

        //Log.v("SA","Hiding Loading VIew");
        view.findViewById(R.id.textInputLayout).setVisibility(View.GONE);
        view.findViewById(R.id.sendloadingPanel).setVisibility(View.GONE);
        view.findViewById(R.id.send_failed_txt).setVisibility(View.GONE);
        view.findViewById(R.id.send_success_txt).setVisibility(View.GONE);

        //Log.v("SA","Hiding Done Loading VIew");
        dest_acc_id = view.findViewById(R.id.dest_id);
        send_amt = view.findViewById(R.id.send_amt);
        memo_txt = view.findViewById(R.id.memo_txt);
        send_btn = view.findViewById(R.id.send_btn);

        spinner = view.findViewById(R.id.asset_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource( this.getActivity() ,
                R.array.asset_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        chkbox_btn = view.findViewById(R.id.checkBox2);

        chkbox_btn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
            if (isChecked) {
                getView().findViewById(R.id.textInputLayout).setVisibility(View.VISIBLE);
                } else {
                getView().findViewById(R.id.textInputLayout).setVisibility(View.GONE);
                }
            }
          }
        );

        send_btn.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {

        if(dest_acc_id.getText().toString().equals("") || send_amt.getText().toString().equals("")) {
            Toast toast = Toast.makeText(getActivity().getApplicationContext(), "Oops, Please add both destination and amount!", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        } else {
            Async4 async4 = new Async4();
            async4.execute((Void) null);
        }
    }

    class Async4 extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            //Log.v("SA","OnPreExecute");

            getView().findViewById(R.id.sendloadingPanel).setVisibility(View.VISIBLE);
            getView().findViewById(R.id.textInputLayout).setVisibility(View.GONE);
            getView().findViewById(R.id.memo_txt).setVisibility(View.GONE);
            getView().findViewById(R.id.send_btn).setVisibility(View.GONE);
            getView().findViewById(R.id.textInputLayout2).setVisibility(View.GONE);
            getView().findViewById(R.id.dest_id).setVisibility(View.GONE);
            getView().findViewById(R.id.textInputLayout3).setVisibility(View.GONE);
            getView().findViewById(R.id.send_amt).setVisibility(View.GONE);
            getView().findViewById(R.id.checkBox2).setVisibility(View.GONE);
            getView().findViewById(R.id.imageView3).setVisibility(View.GONE);
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            try {
                String secretKey = (new Decryptor()).decryptData(getContext(), "SecretKeyAlias_PEDITY6");
                Network.usePublicNetwork();
                Server server = new Server("https://horizon.stellar.org");

                KeyPair source = KeyPair.fromSecretSeed(secretKey);
                KeyPair destination = KeyPair.fromAccountId(dest_acc_id.getText().toString());

                server.accounts().account(destination);

                AccountResponse sourceAccount = server.accounts().account(source);
                Asset asset;
                KeyPair issuer;
                String spintext = spinner.getSelectedItem().toString();
                if(spintext.equals("PEDI")) {
                    issuer = KeyPair.fromAccountId("GBVUDZLMHTLMZANLZB6P4S4RYF52MVWTYVYXTQ2EJBPBX4DZI2SDOLLY");
                    asset = Asset.createNonNativeAsset("PEDI", issuer);
                }
                else if(spintext.equals("SLT")) {
                    issuer = KeyPair.fromAccountId("GCKA6K5PCQ6PNF5RQBF7PQDJWRHO6UOGFMRLK3DYHDOI244V47XKQ4GP");
                    asset = Asset.createNonNativeAsset("SLT", issuer);
                }
                else if(spintext.equals("MOBI")) {
                    issuer = KeyPair.fromAccountId("GA6HCMBLTZS5VYYBCATRBRZ3BZJMAFUDKYYF6AH6MVCMGWMRDNSWJPIH");
                    asset = Asset.createNonNativeAsset("MOBI", issuer);
                }
                else if(spintext.equals("RMT")) {
                    issuer = KeyPair.fromAccountId("GDEGOXPCHXWFYY234D2YZSPEJ24BX42ESJNVHY5H7TWWQSYRN5ZKZE3N");
                    asset = Asset.createNonNativeAsset("RMT", issuer);
                }
                else if(spintext.equals("FRAS")) {
                    issuer = KeyPair.fromAccountId("GC75WHUIMU7LV6WURMCA5GGF2S5FWFOK7K5VLR2WGRKWKZQAJQEBM53M");
                    asset = Asset.createNonNativeAsset("FRAS", issuer);
                }
                else if(spintext.equals("TERN")) {
                    issuer = KeyPair.fromAccountId("GDGQDVO6XPFSY4NMX75A7AOVYCF5JYGW2SHCJJNWCQWIDGOZB53DGP6C");
                    asset = Asset.createNonNativeAsset("TERN", issuer);
                }
                else {
                    asset = new AssetTypeNative();
                }
                if (memo_txt.getText().toString().equals("")) {
                    Transaction transaction = new Transaction.Builder(sourceAccount)
                            .addOperation(new PaymentOperation.Builder(destination, asset, send_amt.getText().toString()).build())
                            .setTimeout(Transaction.Builder.TIMEOUT_INFINITE)
                            .build();
                    transaction.sign(source);

                    SubmitTransactionResponse response = server.submitTransaction(transaction);
                } else {
                    Transaction transaction = new Transaction.Builder(sourceAccount)
                            .addOperation(new PaymentOperation.Builder(destination, asset, send_amt.getText().toString()).build())
                            .addMemo(Memo.text(memo_txt.getText().toString()))
                            .setTimeout(Transaction.Builder.TIMEOUT_INFINITE)
                            .build();
                    transaction.sign(source);

                    SubmitTransactionResponse response = server.submitTransaction(transaction);
                }

            }
            catch (final Exception e) {
                return false;
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean val) {
            getView().findViewById(R.id.sendloadingPanel).setVisibility(View.GONE);
            if(val) {
                getView().findViewById(R.id.send_success_txt).setVisibility(View.VISIBLE);
            }
            else {
                getView().findViewById(R.id.send_failed_txt).setVisibility(View.VISIBLE);
            }
        }
    }
}
