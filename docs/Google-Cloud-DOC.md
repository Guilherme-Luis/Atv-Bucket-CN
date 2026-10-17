Use streaming to upload an object in a Cloud Storage bucket.

## Explore further


For detailed documentation that includes this code sample, see the following:

- [Streaming uploads](https://docs.cloud.google.com/storage/docs/streaming-uploads)

## Code sample

### C++


For more information, see the
[Cloud Storage C++ API
reference documentation](https://docs.cloud.google.com/cpp/docs/reference/storage/latest).


To authenticate to Cloud Storage, set up Application Default Credentials.
For more information, see

[Set up authentication for client libraries](https://docs.cloud.google.com/storage/docs/authentication#client-libs).

    namespace gcs = ::google::cloud::storage;
    using ::google::cloud::StatusOr;
    [](gcs::Client client, std::string const& bucket_name,
       std::string const& object_name, int desired_line_count) {
      std::string const text = "Lorem ipsum dolor sit amet";
      gcs::ObjectWriteStream stream =
          client.WriteObject(bucket_name, object_name);

      for (int lineno = 0; lineno != desired_line_count; ++lineno) {
        // Add 1 to the counter, because it is conventional to number lines
        // starting at 1.
        stream << (lineno + 1) << ": " << text << "\n";
      }

      stream.Close();

      StatusOr<gcs::ObjectMetadata> metadata = std::move(stream).metadata();
      if (!metadata) throw std::move(metadata).status();
      std::cout << "Successfully wrote to object " << metadata->name()
                << " its size is: " << metadata->size()
                << "\nFull metadata: " << *metadata << "\n";
    }

### C#


For more information, see the
[Cloud Storage C# API
reference documentation](https://cloud.google.com/dotnet/docs/reference/Google.Cloud.Storage.V1/latest).


To authenticate to Cloud Storage, set up Application Default Credentials.
For more information, see

[Set up authentication for client libraries](https://docs.cloud.google.com/storage/docs/authentication#client-libs).


    using https://docs.cloud.google.com/dotnet/docs/reference/Google.Cloud.Storage.V1/latest/Google.Cloud.Storage.V1.html;
    using System;
    using System.IO;

    public class UploadFileSample
    {
        public void UploadFile(
            string bucketName = "your-unique-bucket-name",
            string localPath = "my-local-path/my-file-name",
            string objectName = "my-file-name")
        {
            var storage = https://docs.cloud.google.com/dotnet/docs/reference/Google.Cloud.Storage.V1/latest/Google.Cloud.Storage.V1.StorageClient.html.https://docs.cloud.google.com/dotnet/docs/reference/Google.Cloud.Storage.V1/latest/Google.Cloud.Storage.V1.StorageClient.html#Google_Cloud_Storage_V1_StorageClient_Create();
            using var fileStream = File.OpenRead(localPath);
            storage.UploadObject(bucketName, objectName, null, fileStream);
            Console.WriteLine($"Uploaded {objectName}.");
        }
    }

### Go


For more information, see the
[Cloud Storage Go API
reference documentation](https://pkg.go.dev/cloud.google.com/go/storage).


To authenticate to Cloud Storage, set up Application Default Credentials.
For more information, see

[Set up authentication for client libraries](https://docs.cloud.google.com/storage/docs/authentication#client-libs).

    import (
    	"bytes"
    	"context"
    	"fmt"
    	"io"
    	"time"

    	"cloud.google.com/go/storage"
    )

    // streamFileUpload uploads an object via a stream.
    func streamFileUpload(w io.https://docs.cloud.google.com/go/docs/reference/cloud.google.com/go/storage/latest/index.html#cloud_google_com_go_storage_Writer, bucket, object string) error {
    	// bucket := "bucket-name"
    	// object := "object-name"
    	ctx := context.Background()
    	client, err := storage.NewClient(ctx)
    	if err != nil {
    		return fmt.Errorf("storage.NewClient: %w", err)
    	}
    	defer client.Close()

    	b := []byte("Hello world.")
    	buf := bytes.NewBuffer(b)

    	ctx, cancel := context.WithTimeout(ctx, time.Second*50)
    	defer cancel()

    	// Upload an object with storage.Writer.
    	wc := client.https://docs.cloud.google.com/go/docs/reference/cloud.google.com/go/storage/latest/index.html#cloud_google_com_go_storage_Client_Bucket(bucket).https://docs.cloud.google.com/go/docs/reference/cloud.google.com/go/storage/latest/index.html#cloud_google_com_go_storage_BucketHandle_Object(object).https://docs.cloud.google.com/go/docs/reference/cloud.google.com/go/storage/latest/index.html#cloud_google_com_go_storage_ObjectHandle_NewWriter(ctx)
    	wc.ChunkSize = 0 // note retries are not supported for chunk size 0.

    	if _, err = io.Copy(wc, buf); err != nil {
    		return fmt.Errorf("io.Copy: %w", err)
    	}
    	// Data can continue to be added to the file until the writer is closed.
    	if err := wc.Close(); err != nil {
    		return fmt.Errorf("Writer.Close: %w", err)
    	}
    	fmt.Fprintf(w, "%v uploaded to %v.\n", object, bucket)

    	return nil
    }

### Java


For more information, see the
[Cloud Storage Java API
reference documentation](https://cloud.google.com/java/docs/reference/google-cloud-storage/latest/overview).


To authenticate to Cloud Storage, set up Application Default Credentials.
For more information, see

[Set up authentication for client libraries](https://docs.cloud.google.com/storage/docs/authentication#client-libs).


    import com.google.cloud.https://docs.cloud.google.com/java/docs/reference/google-cloud-core/latest/com.google.cloud.WriteChannel.html;
    import com.google.cloud.storage.https://docs.cloud.google.com/java/docs/reference/google-cloud-storage/latest/com.google.cloud.storage.BlobId.html;
    import com.google.cloud.storage.https://docs.cloud.google.com/java/docs/reference/google-cloud-storage/latest/com.google.cloud.storage.BlobInfo.html;
    import com.google.cloud.storage.https://docs.cloud.google.com/java/docs/reference/google-cloud-storage/latest/com.google.cloud.storage.Storage.html;
    import com.google.cloud.storage.https://docs.cloud.google.com/java/docs/reference/google-cloud-storage/latest/com.google.cloud.storage.StorageOptions.html;
    import java.io.IOException;
    import java.nio.ByteBuffer;
    import java.nio.charset.StandardCharsets;

    public class StreamObjectUpload {

      public static void streamObjectUpload(
          https://docs.cloud.google.com/java/docs/reference/google-cloud-bigtable/latest/com.google.cloud.bigtable.common.Type.String.html projectId, https://docs.cloud.google.com/java/docs/reference/google-cloud-bigtable/latest/com.google.cloud.bigtable.common.Type.String.html bucketName, https://docs.cloud.google.com/java/docs/reference/google-cloud-bigtable/latest/com.google.cloud.bigtable.common.Type.String.html objectName, https://docs.cloud.google.com/java/docs/reference/google-cloud-bigtable/latest/com.google.cloud.bigtable.common.Type.String.html contents) throws IOException {
        // The ID of your GCP project
        // String projectId = "your-project-id";

        // The ID of your GCS bucket
        // String bucketName = "your-unique-bucket-name";

        // The ID of your GCS object
        // String objectName = "your-object-name";

        // The string of contents you wish to upload
        // String contents = "Hello world!";

        https://docs.cloud.google.com/java/docs/reference/google-cloud-storage/latest/com.google.cloud.storage.Storage.html storage = https://docs.cloud.google.com/java/docs/reference/google-cloud-storage/latest/com.google.cloud.storage.StorageOptions.html.newBuilder().setProjectId(projectId).build().getService();
        https://docs.cloud.google.com/java/docs/reference/google-cloud-storage/latest/com.google.cloud.storage.BlobId.html blobId = https://docs.cloud.google.com/java/docs/reference/google-cloud-storage/latest/com.google.cloud.storage.BlobId.html.of(bucketName, objectName);
        https://docs.cloud.google.com/java/docs/reference/google-cloud-storage/latest/com.google.cloud.storage.BlobInfo.html blobInfo = https://docs.cloud.google.com/java/docs/reference/google-cloud-storage/latest/com.google.cloud.storage.BlobInfo.html.newBuilder(blobId).build();
        byte[] content = contents.getBytes(StandardCharsets.UTF_8);
        try (https://docs.cloud.google.com/java/docs/reference/google-cloud-core/latest/com.google.cloud.WriteChannel.html writer = storage.https://docs.cloud.google.com/java/docs/reference/google-cloud-storage/latest/com.google.cloud.storage.Storage.html#com_google_cloud_storage_Storage_writer_com_google_cloud_storage_BlobInfo_com_google_cloud_storage_Storage_BlobWriteOption____(blobInfo)) {
          writer.write(ByteBuffer.wrap(content));
          System.out.println(
              "Wrote to " + objectName + " in bucket " + bucketName + " using a WriteChannel.");
        }
      }
    }

### Node.js


For more information, see the
[Cloud Storage Node.js API
reference documentation](https://cloud.google.com/nodejs/docs/reference/storage/latest).


To authenticate to Cloud Storage, set up Application Default Credentials.
For more information, see

[Set up authentication for client libraries](https://docs.cloud.google.com/storage/docs/authentication#client-libs).

    /**
     * TODO(developer): Uncomment the following lines before running the sample
     */
    // The ID of your GCS bucket
    // const bucketName = 'your-unique-bucket-name';

    // The new ID for your GCS file
    // const destFileName = 'your-new-file-name';

    // The content to be uploaded in the GCS file
    // const contents = 'your file content';

    // Imports the Google Cloud client library
    const {Storage} = require('https://docs.cloud.google.com/nodejs/docs/reference/storage/latest/overview.html');

    // Import Node.js stream
    const stream = require('stream');

    // Creates a client
    const storage = new https://docs.cloud.google.com/nodejs/docs/reference/storage-control/latest/storage-control/protos.google.storage.v2.storage-class.html();

    // Get a reference to the bucket
    const myBucket = storage.bucket(bucketName);

    // Create a reference to a file object
    const file = myBucket.file(destFileName);

    // Create a pass through stream from a string
    const passthroughStream = new stream.PassThrough();
    passthroughStream.write(contents);
    passthroughStream.end();

    async function streamFileUpload() {
      passthroughStream.pipe(file.https://docs.cloud.google.com/nodejs/docs/reference/storage/latest/storage/file_2.html()).on('finish', () => {
        // The file upload is complete
      });

      console.log(`${destFileName} uploaded to ${bucketName}`);
    }

    streamFileUpload().catch(console.error);

### PHP


For more information, see the
[Cloud Storage PHP API
reference documentation](https://googleapis.github.io/google-cloud-php/#/docs/google-cloud/latest/storage/storageclient).


To authenticate to Cloud Storage, set up Application Default Credentials.
For more information, see

[Set up authentication for client libraries](https://docs.cloud.google.com/storage/docs/authentication#client-libs).

    use Google\Cloud\Storage\StorageClient;
    use Google\Cloud\Storage\WriteStream;

    /**
     * Upload a chunked file stream.
     *
     * @param string $bucketName The name of your Cloud Storage bucket.
     *        (e.g. 'my-bucket')
     * @param string $objectName The name of your Cloud Storage object.
     *        (e.g. 'my-object')
     * @param string $contents The contents to upload via stream chunks.
     *        (e.g. 'these are my contents')
     */
    function upload_object_stream(string $bucketName, string $objectName, string $contents): void
    {
        $storage = new StorageClient();
        $bucket = $storage->bucket($bucketName);
        $writeStream = new WriteStream(null, [
            'chunkSize' => 1024 * 256, // 256KB
        ]);
        $uploader = $bucket->getStreamableUploader($writeStream, [
            'name' => $objectName,
        ]);
        $writeStream->setUploader($uploader);
        $stream = fopen('data://text/plain,' . $contents, 'r');
        while (($line = stream_get_line($stream, 1024 * 256)) !== false) {
            $writeStream->write($line);
        }
        $writeStream->close();

        printf('Uploaded %s to gs://%s/%s' . PHP_EOL, $contents, $bucketName, $objectName);
    }

### Python


For more information, see the
[Cloud Storage Python API
reference documentation](https://cloud.google.com/python/docs/reference/storage/latest).


To authenticate to Cloud Storage, set up Application Default Credentials.
For more information, see

[Set up authentication for client libraries](https://docs.cloud.google.com/storage/docs/authentication#client-libs).

    from google.cloud import https://docs.cloud.google.com/python/docs/reference/storage/latest


    def upload_blob_from_stream(bucket_name, file_obj, destination_blob_name):
        """Uploads bytes from a stream or other file-like object to a blob."""
        # The ID of your GCS bucket
        # bucket_name = "your-bucket-name"

        # The stream or file (file-like object) from which to read
        # import io
        # file_obj = io.BytesIO()
        # file_obj.write(b"This is test data.")

        # The desired name of the uploaded GCS object (blob)
        # destination_blob_name = "storage-object-name"

        # Construct a client-side representation of the blob.
        storage_client = https://docs.cloud.google.com/python/docs/reference/storage/latest.https://docs.cloud.google.com/python/docs/reference/storage/latest/google.cloud.storage.client.Client.html()
        bucket = storage_client.https://docs.cloud.google.com/python/docs/reference/storage/latest/google.cloud.storage.client.Client.html#google_cloud_storage_client_Client_bucket(bucket_name)
        blob = bucket.blob(destination_blob_name)

        # Rewind the stream to the beginning. This step can be omitted if the input
        # stream will always be at a correct position.
        file_obj.seek(0)

        # Upload data from the stream to your bucket.
        blob.https://docs.cloud.google.com/python/docs/reference/storage/latest/google.cloud.storage.blob.Blob.html#google_cloud_storage_blob_Blob_upload_from_file(file_obj)

        print(
            f"Stream data uploaded to {destination_blob_name} in bucket {bucket_name}."
        )

### Ruby


For more information, see the
[Cloud Storage Ruby API
reference documentation](https://googleapis.dev/ruby/google-cloud-storage/latest/Google/Cloud/Storage.html).


To authenticate to Cloud Storage, set up Application Default Credentials.
For more information, see

[Set up authentication for client libraries](https://docs.cloud.google.com/storage/docs/authentication#client-libs).


    # The ID of your GCS bucket
    # bucket_name = "your-unique-bucket-name"

    # The stream or file (file-like object) from which to read
    # local_file_obj = StringIO.new "This is test data."

    # Name of a file in the Storage bucket
    # file_name   = "some_file.txt"

    require "google/cloud/storage"

    storage = Google::Cloud::https://docs.cloud.google.com/ruby/docs/reference/google-cloud-storage-control-v2/latest/Google-Cloud-Storage.html.https://docs.cloud.google.com/ruby/docs/reference/google-cloud-storage/latest/Google-Cloud-Storage.html
    bucket  = storage.bucket bucket_name

    local_file_obj.rewind
    bucket.https://docs.cloud.google.com/ruby/docs/reference/google-cloud-storage/latest/Google-Cloud-Storage-Bucket.html local_file_obj, file_name

    puts "Stream data uploaded to #{file_name} in bucket #{bucket_name}"

## What's next


To search and filter code samples for other Google Cloud products, see the
[Google Cloud sample browser](https://docs.cloud.google.com/docs/samples?product=storage).