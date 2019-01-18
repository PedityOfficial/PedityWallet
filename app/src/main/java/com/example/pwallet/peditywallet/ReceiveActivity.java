package com.example.pwallet.peditywallet;

import android.os.Bundle;
import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.os.AsyncTask;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.Button;
import android.widget.Toast;

import org.stellar.sdk.KeyPair;
import org.stellar.sdk.Network;
import org.stellar.sdk.Server;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class ReceiveActivity extends Fragment {
    private String accid;
    private TextView accountId;
    ImageView qrCode;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_receive, container, false);

        view.findViewById(R.id.textView).setVisibility(View.GONE);
        view.findViewById(R.id.textView2).setVisibility(View.GONE);
        view.findViewById(R.id.textView4).setVisibility(View.GONE);
        view.findViewById(R.id.button2).setVisibility(View.GONE);
        view.findViewById(R.id.imageView2).setVisibility(View.GONE);

        Async2 mAuthTask = new Async2();
        mAuthTask.execute((Void) null);

        return view;
    }

    class Async2 extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {

            try {
                String secretKey = (new Decryptor()).decryptData(getContext(), "SecretKeyAlias_PEDITY6");
                Network.usePublicNetwork();
                Server server = new Server("https://horizon.stellar.org");

                KeyPair keyPair = KeyPair.fromSecretSeed(secretKey);

                accid = keyPair.getAccountId();
                //Log.v("RA", accid);
                // Log.v("RA", server.operations().forAccount(keyPair));
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        accountId = getView().findViewById(R.id.textView2);
                        accountId.setText(accid);
                    }
                });

                final Button button = getView().findViewById(R.id.button2);

                final TextView textdata = getView().findViewById(R.id.textView2);

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ClipboardManager myClipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);

                        String text;
                        text = textdata.getText().toString();

                        ClipData myClip = ClipData.newPlainText("text", text);
                        myClipboard.setPrimaryClip(myClip);

                        Toast.makeText(getActivity().getApplicationContext(), "Address Copied",Toast.LENGTH_SHORT).show();
                    }
                });

                qrCode=getView().findViewById(R.id.imageView2);
                MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
                try {
                    BitMatrix bitMatrix = multiFormatWriter.encode(accid, BarcodeFormat.QR_CODE,600,600);
                    BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                    final Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                        qrCode.setImageBitmap(bitmap);
                        }
                    });
                } catch (WriterException e) {
                    e.printStackTrace();
                }
            }
            catch (Exception e) {
                //Log.v("Error in RA", e.getMessage());
                return false;
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (result) {
                getView().findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                getView().findViewById(R.id.textView).setVisibility(View.VISIBLE);
                getView().findViewById(R.id.textView2).setVisibility(View.VISIBLE);
                getView().findViewById(R.id.textView4).setVisibility(View.VISIBLE);
                getView().findViewById(R.id.button2).setVisibility(View.VISIBLE);
                getView().findViewById(R.id.imageView2).setVisibility(View.VISIBLE);
            }
        }
    }
}

