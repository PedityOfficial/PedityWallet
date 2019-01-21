import android.support.design.widget.TextInputEditText;

import org.stellar.sdk.AssetTypeNative;
import org.stellar.sdk.KeyPair;
import org.stellar.sdk.Memo;
import org.stellar.sdk.Network;
import org.stellar.sdk.PaymentOperation;
import org.stellar.sdk.Server;
import org.stellar.sdk.Transaction;
import org.stellar.sdk.responses.AccountResponse;
import org.stellar.sdk.responses.SubmitTransactionResponse;

import java.io.InputStream;
import java.net.URL;
import java.security.Key;
import java.util.Scanner;

public class test {
    public static void main(String[] args) {
        try {
            /*KeyPair pair = KeyPair.fromSecretSeed("f138535689e81753770768140de5cace0fbb7464d45f5f776eeff9e5235219ad");
            Server server = new Server("https://horizon.stellar.org");
            System.out.println(server.accounts().account(pair));
            System.out.println("HAHA ");*/
            /*KeyPair pair = KeyPair.random();
            String friendbotUrl = String.format(
                    "https://friendbot.stellar.org/?addr=%s",
                    pair.getAccountId());
            InputStream response = new URL(friendbotUrl).openStream();
            String body = new Scanner(response, "UTF-8").useDelimiter("\\A").next();
            System.out.println("SUCCESS" + pair.getSecretSeed() + " and " + pair.getAccountId());
            KeyPair newPair = KeyPair.fromSecretSeed(pair.getSecretSeed());
            Server server = new Server("https://horizon.stellar.org");
            System.out.println(server.accounts().account(newPair));
            System.out.println(newPair.getAccountId());
            System.out.println(newPair.getSecretSeed());*/
            Network.usePublicNetwork();
            //TextInputEditText editText = (TextInputEditText)
            Server server = new Server("https://horizon.stellar.org");
            //KeyPair again = KeyPair.fromSecretSeed("SAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAABSU2"); //[C@46f5f779 GDRFTQD4XSIWPZ7TAPVKYEM2BWAU6JDTOMVVFQ3MWTS2QXG3W3ML2MF4
            KeyPair source = KeyPair.fromSecretSeed("SAKRB7EE6H23EF733WFU76RPIYOPEWVOMBBUXDQYQ3OF4NF6ZY6B6VLW");
                server.accounts().account(source);

                KeyPair destination = KeyPair.fromAccountId("GA2C5RFPE6GCKMY3US5PAB6UZLKIGSPIUKSLRB6Q723BM2OARMDUYEJ5");


            //System.out.println(accountResponse);
            /*System.out.println(again.getAccountId());
            System.out.println(dest.getAccountId());
            AccountResponse destAccount = server.accounts().account(dest);*/
            //System.out.println(destAccount);

            AccountResponse sourceAccount = server.accounts().account(source);

            Transaction transaction = new Transaction.Builder(sourceAccount)
                    .addOperation(new PaymentOperation.Builder(destination, new AssetTypeNative(), "2").build())
                    // A memo allows you to add your own metadata to a transaction. It's
                    // optional and does not affect how Stellar treats the transaction.
                    .addMemo(Memo.text("Test"))
                    .build();
            // Sign the transaction to prove you are actually the person sending it.
            transaction.sign(source);

            SubmitTransactionResponse response = server.submitTransaction(transaction);
            System.out.println("Response " + response);

            //System.out.println(pair.getSecretSeed());

        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
}

