var params = {
  TableName: 'fuzo-content',
  KeySchema: [
    {
      AttributeName: 'id',
      KeyType: 'HASH'
    }
  ],
  AttributeDefinitions: [
    {
      AttributeName: 'id',
      AttributeType: 'N'
    }
  ],
  ProvisionedThroughput: {
    ReadCapacityUnits: 10,
    WriteCapacityUnits: 10
  }
};

dynamodb.createTable(params, function(err, data) {
if (err) ppJson(err); // an error occurred
else ppJson(data); // successful response
});