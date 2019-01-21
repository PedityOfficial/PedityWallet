package com.example.pwallet.peditywallet;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
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

public class SendPage extends AppCompatActivity implements View.OnClickListener {

    private EditText dest_acc_id;
    private EditText send_amt;
    private EditText memo_txt;
    Button send_btn;
    Toolbar toolbar;
    private CheckBox chkbox_btn;
    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_page);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Send");
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setIcon(R.drawable.ic_main_dashboard);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        findViewById(R.id.memo_txt).setVisibility(View.GONE);
        findViewById(R.id.sendloadingPanel2).setVisibility(View.GONE);
        findViewById(R.id.send_failed_txt).setVisibility(View.GONE);
        findViewById(R.id.send_success_txt).setVisibility(View.GONE);

        dest_acc_id = findViewById(R.id.dest_id);
        send_amt = findViewById(R.id.send_amt);
        memo_txt = findViewById(R.id.memo_txt);
        send_btn = findViewById(R.id.send_btn);

        spinner = findViewById(R.id.asset_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource( this ,
                R.array.asset_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        chkbox_btn = findViewById(R.id.checkBox2);

        chkbox_btn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                                  @Override
                                                  public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                                                      if (isChecked) {
                                                          findViewById(R.id.memo_txt).setVisibility(View.VISIBLE);
                                                      } else {
                                                          findViewById(R.id.memo_txt).setVisibility(View.GONE);
                                                      }
                                                  }
                                              }
        );

        if(getIntent().getExtras() != null &&  getIntent().getExtras().containsKey("Dest_Acc_Id")) {
            dest_acc_id.setText((String) getIntent().getExtras().get("Dest_Acc_Id"));
        }
        send_btn.setOnClickListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if(dest_acc_id.getText().toString().equals("") || send_amt.getText().toString().equals("")) {
            Toast toast = Toast.makeText(this.getApplicationContext(), "Oops, Please add both destination and amount!", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        } else {
            Async1 async1 = new Async1(getApplicationContext());
            async1.execute((Void) null);
        }
    }


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
    }

    class Async1 extends AsyncTask<Void, Void, Boolean> {

        Context context;
        private Async1(Context icontext) {
            context = icontext;
        }

        @Override
        protected void onPreExecute() {
            //Log.v("SA","OnPreExecute");

            findViewById(R.id.sendloadingPanel2).setVisibility(View.VISIBLE);
            findViewById(R.id.send_form).setVisibility(View.GONE);
            findViewById(R.id.checkBox2).setVisibility(View.GONE);
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            try {
                String secretKey = (new Decryptor()).decryptData(getApplicationContext(), "SecretKeyAlias_PEDITY6");
                Network.usePublicNetwork();
                Server server = new Server("https://horizon.stellar.org");

                KeyPair source = KeyPair.fromSecretSeed(secretKey);
                KeyPair destination = KeyPair.fromAccountId(dest_acc_id.getText().toString());

                server.accounts().account(destination);

                AccountResponse sourceAccount = server.accounts().account(source);
                KeyPair issuer = KeyPair.fromAccountId("GBVUDZLMHTLMZANLZB6P4S4RYF52MVWTYVYXTQ2EJBPBX4DZI2SDOLLY");
                Asset pediasset = Asset.createNonNativeAsset("PEDI", issuer);

                String spintext = spinner.getSelectedItem().toString();
                if(spintext.equals("PEDI")) {
                    if (memo_txt.getText().toString().equals("")) {
                        Transaction transaction = new Transaction.Builder(sourceAccount)
                                .addOperation(new PaymentOperation.Builder(destination, pediasset, send_amt.getText().toString()).build())
                                .setTimeout(Transaction.Builder.TIMEOUT_INFINITE)
                                .build();
                        transaction.sign(source);

                        SubmitTransactionResponse response = server.submitTransaction(transaction);
                    } else {
                        Transaction transaction = new Transaction.Builder(sourceAccount)
                                .addOperation(new PaymentOperation.Builder(destination, pediasset, send_amt.getText().toString()).build())
                                .addMemo(Memo.text(memo_txt.getText().toString()))
                                .setTimeout(Transaction.Builder.TIMEOUT_INFINITE)
                                .build();
                        transaction.sign(source);

                        SubmitTransactionResponse response = server.submitTransaction(transaction);
                    }
                } else {
                    if (memo_txt.getText().toString().equals("")) {
                        Transaction transaction = new Transaction.Builder(sourceAccount)
                                .addOperation(new PaymentOperation.Builder(destination, new AssetTypeNative(), send_amt.getText().toString()).build())
                                .setTimeout(Transaction.Builder.TIMEOUT_INFINITE)
                                .build();
                        transaction.sign(source);

                        SubmitTransactionResponse response = server.submitTransaction(transaction);
                    } else {
                        Transaction transaction = new Transaction.Builder(sourceAccount)
                                .addOperation(new PaymentOperation.Builder(destination, new AssetTypeNative(), send_amt.getText().toString()).build())
                                .addMemo(Memo.text(memo_txt.getText().toString()))
                                .setTimeout(Transaction.Builder.TIMEOUT_INFINITE)
                                .build();
                        transaction.sign(source);

                        SubmitTransactionResponse response = server.submitTransaction(transaction);
                    }
                }
            }
            catch (final Exception e) {
                return false;
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean val) {
            //Log.v("SA","OnPostExecute");
            findViewById(R.id.sendloadingPanel2).setVisibility(View.GONE);
            if(val) {
                findViewById(R.id.send_success_txt).setVisibility(View.VISIBLE);
            }
            else {
                findViewById(R.id.send_failed_txt).setVisibility(View.VISIBLE);

            }
        }
    }
}
