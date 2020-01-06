package ucd.creativecomputation.toolkit.retriever;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;

import java.io.*;
import java.net.URL;

import static ucd.creativecomputation.alexa.NarratorStreamHandler.IAM_ACCESSKEY;
import static ucd.creativecomputation.alexa.NarratorStreamHandler.IAM_SECRETKEY;

/**
 * Class to read from Amazon S3 and to retrieve file URLs. These are needed
 * to create the Knowledge Data, since the Knowledge Base Files are inside an
 * Amazon S3 bucket.
 *
 * @author
 * Thomas Mildner
 */
public class S3FileRetriever {

    // Setup variables.
    private AmazonS3 s3Client   = null;
    private String bucketName   = null;
    private String fileName     = null;

    /**
     * Constructor, that will read from Amazon S3
     * @param bucketName
     *  The name of a bucket inside Amazon S3.
     * @param fileName
     *  The name of a file. This needs to be a filename that exists inside the bucket, otherwise
     *  an error will occur.
     */
    public S3FileRetriever(String bucketName, String fileName){
        this.bucketName = bucketName;
        this.fileName   = fileName;

        readFromS3Bucket();
    }

    /**
     * Function to read from Amazon S3. This class sets up needed credentials
     * and a client
     */
    public void readFromS3Bucket(){

        // Setting the credentials. Both keys are provided in the NarratorStreamHandler Class.
        AWSCredentials credentials = new BasicAWSCredentials(
                IAM_ACCESSKEY,
                IAM_SECRETKEY);

        // Setting the Client Object which will give access to the files inside the S3Bucket.
        s3Client = AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(Regions.US_EAST_1)
                .build();
    }

    /**
     * Method to return an InputStream from a file in an Amazon S3 bucket.
     * @return
     *  returns an InputStream from a file in an Amazon S3 bucket.
     */
    public InputStream getInputStreamFromS3File(){
        return s3Client.getObject(bucketName, fileName).getObjectContent();
    }

    /**
     * Method to return the URL from a file in an Amazon S3 bucket.
     * @return
     *  returns the URL from a file in an Amazon S3 bucket.
     */
    public URL getURLFromS3File(){
        return s3Client.getUrl(bucketName, fileName);
    }


    /////////////////////////////////
    public static void main(String[] args) {
        S3FileRetriever fileRetriever = new S3FileRetriever("knowledge-based-modul-data", "Veale's The NOC List.txt");
        fileRetriever.getInputStreamFromS3File();
        System.out.println(fileRetriever.getURLFromS3File());

        // Listing all files inside the bucket
        ObjectListing objectListing = fileRetriever.s3Client.listObjects(fileRetriever.bucketName);
        System.out.println("The Bucket " + fileRetriever.bucketName + " contains the following files:\n");
        for(S3ObjectSummary os : objectListing.getObjectSummaries()) {
            System.out.println(os.getKey());
        }

    }
}
