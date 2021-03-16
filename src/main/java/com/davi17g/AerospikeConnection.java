package com.davi17g;

import com.aerospike.client.AerospikeClient;
import com.aerospike.client.AerospikeException;
import com.aerospike.client.Bin;
import com.aerospike.client.Key;
import com.aerospike.client.Record;
import com.aerospike.client.policy.ClientPolicy;
import com.aerospike.client.policy.Policy;
import com.aerospike.client.util.RandomShift;


public class AerospikeConnection {
    private static AerospikeConnection connection = null;
    public RandomShift randomShift = null;
    public AerospikeClient client = null;

    private int IdSize = 0;

    private AerospikeConnection(String host, int port, int idSize) throws AerospikeException {
        ClientPolicy clientPolicy = new ClientPolicy();
        this.client = new AerospikeClient(clientPolicy, host, port);
        this.randomShift = new RandomShift();
        this.IdSize = idSize;

    }

    public static AerospikeConnection getInstance(String host, int port, int IdSize) throws AerospikeException{

        if (connection == null) {
            connection = new AerospikeConnection(host, port, IdSize);

        }
        return connection;
    }

    public String Set(String url) throws AerospikeException {

        Key key = new Key("urls",null, this.genKey());
        while (this.client.exists(null, key)) {
            key = new Key("urls",null, this.genKey());
        }
        this.client.put(null, key, new Bin("urls", url));
        return key.userKey.toString();

    }

    public String Get(String key) throws AerospikeException {

        Policy policy = new Policy();
        Record record = this.client.get(policy, new Key("urls", null, key));
        return record.getString("urls");
    }

    synchronized private String genKey() {
        return randomShift.nextString(this.IdSize);
    }


}
