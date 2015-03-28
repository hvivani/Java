//http://docs.aws.amazon.com/AWSSdkDocsJava/latest/DeveloperGuide/credentials.html
//http://docs.aws.amazon.com/ElasticMapReduce/latest/DeveloperGuide/calling-emr-with-java-sdk.html
//http://docs.aws.amazon.com/ElasticMapReduce/latest/DeveloperGuide/emr-common-programming-sample.html
//http://docs.aws.amazon.com/ElasticMapReduce/latest/DeveloperGuide/LaunchingSpotInstances.html

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.Date;


//import org.json.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import com.amazonaws.AmazonWebServiceClient;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.elasticmapreduce.*;
import com.amazonaws.services.elasticmapreduce.model.AddInstanceGroupsRequest;
import com.amazonaws.services.elasticmapreduce.model.AddJobFlowStepsRequest;
import com.amazonaws.services.elasticmapreduce.model.AddJobFlowStepsResult;
import com.amazonaws.services.elasticmapreduce.model.DescribeClusterRequest;
import com.amazonaws.services.elasticmapreduce.model.DescribeClusterResult;
import com.amazonaws.services.elasticmapreduce.model.DescribeJobFlowsRequest;
import com.amazonaws.services.elasticmapreduce.model.DescribeJobFlowsResult;
import com.amazonaws.services.elasticmapreduce.model.HadoopJarStepConfig;
import com.amazonaws.services.elasticmapreduce.model.InstanceGroup;
import com.amazonaws.services.elasticmapreduce.model.InstanceGroupConfig;
import com.amazonaws.services.elasticmapreduce.model.InstanceGroupStatus;
import com.amazonaws.services.elasticmapreduce.model.JobFlowExecutionState;
import com.amazonaws.services.elasticmapreduce.model.JobFlowInstancesConfig;
import com.amazonaws.services.elasticmapreduce.model.ListInstanceGroupsRequest;
import com.amazonaws.services.elasticmapreduce.model.ListInstancesRequest;
import com.amazonaws.services.elasticmapreduce.model.MarketType;
import com.amazonaws.services.elasticmapreduce.model.RunJobFlowRequest;
import com.amazonaws.services.elasticmapreduce.model.RunJobFlowResult;
import com.amazonaws.services.elasticmapreduce.model.StepConfig;
import com.amazonaws.services.elasticmapreduce.model.JobFlowDetail;
import com.amazonaws.services.elasticmapreduce.model.transform.InstanceGroupStatusJsonUnmarshaller;
import com.amazonaws.services.elasticmapreduce.util.StepFactory;
import com.amazonaws.services.s3.model.Region;
import com.amazonaws.util.json.JSONException;
import com.amazonaws.util.json.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Main {
	

	public static void main(String[] args) {
		
		//RUN job flow:
		AmazonElasticMapReduce emrClient = new AmazonElasticMapReduceClient(new DefaultAWSCredentialsProviderChain());
		
		emrClient.setRegion(Region.EU_Ireland.toAWSRegion());
		
		//String clusterCreated="";
		
		//REMOVE THIS: just for local tests
		args=new String[1];
		args[0]=getClusterId();
		//"j-3M50UTXXXXXX";
		//REMOVE THIS:
		
		//System.out.println("CreateCluster................................:");
		//clusterCreated=launchCluster(emrClient);
		//System.out.println("ClusterCreated: :" + clusterCreated);
   //     System.out.println("DescribeCluster................................:");
   //     describeCluster(emrClient,args[0].toString());
   //     System.out.println("DescribeClusterInstances.......................:");
   //     describeClusterInstances(emrClient,args[0].toString());
        System.out.println("VerifyHealthyRunningSpotInstances..............:");
        verifyHealthyRunningSpotInstances(emrClient,args[0].toString());
        
        
           
	}
	
	public static String launchCluster(AmazonElasticMapReduce client){
		   RunJobFlowRequest request = new RunJobFlowRequest();
	       request.setName("EMR Java SDK Testings");
	       request.setAmiVersion("3.3.1");
	       //.withSteps(enabledebugging, installHive)
	       request.setLogUri("s3://your-bucket-emr//");
	       //request.setServiceRole("service_role");
	       //request.setJobFlowRole("jobflow_role");
	       

	       JobFlowInstancesConfig instances = new JobFlowInstancesConfig();  
	       	request.setInstances(instances );
		    instances.setEc2KeyName("your-key");
		    instances.setInstanceCount(new Integer(5));
		    instances.setKeepJobFlowAliveWhenNoSteps(new Boolean(true));
		    instances.setMasterInstanceType("m1.medium");
		    instances.setSlaveInstanceType("m1.medium");
		    
		    RunJobFlowResult result = client.runJobFlow(request);		
	        System.out.println("output: " + result.toString());
	        return result.toString();
	}
	
	public static String addStep(AmazonElasticMapReduce client, String clusterId){
		StepConfig hive = new StepConfig("Hive", new StepFactory().newInstallHiveStep());
	    HadoopJarStepConfig hadoopConfig1 = new HadoopJarStepConfig()
        .withJar("s3://mybucket/my-jar-location1")
        .withMainClass("com.my.Main1") // optional main class, this can be omitted if jar above has a manifest
        .withArgs("--verbose"); // optional list of arguments
	    StepConfig customStep = new StepConfig("Step1", hadoopConfig1);

	    AddJobFlowStepsResult result = client.addJobFlowSteps(new AddJobFlowStepsRequest()
        .withJobFlowId(clusterId)
        .withSteps(hive, customStep));
	    System.out.println(result.getStepIds());
	    return result.getStepIds().toString();
	}
	
	public static void describeCluster(AmazonElasticMapReduce client, String clusterId)
	{
        DescribeClusterResult result = client.describeCluster(new DescribeClusterRequest().withClusterId(clusterId));
        
        System.out.println("ClusterId: " + result.getCluster().getId());
        System.out.println("Name: " + result.getCluster().getName());
        System.out.println("State: " + result.getCluster().getStatus().getState());
        System.out.println("Reason: " + result.getCluster().getStatus().getStateChangeReason());
        System.out.println("Timeline: " + result.getCluster().getStatus().getTimeline());		
	}
	
	public static void describeClusterInstances(AmazonElasticMapReduce client, String clusterId)
	{
        List<InstanceGroup> instgrouplist = client.listInstanceGroups(new ListInstanceGroupsRequest().withClusterId(clusterId)).getInstanceGroups();
        for (InstanceGroup instgroup: instgrouplist ){
     	   System.out.println("Type: " + instgroup.getInstanceGroupType());
     	   System.out.println("Status: " + instgroup.getStatus().getState());
     	   System.out.println("Timeline: " + instgroup.getStatus().getTimeline());
     	   System.out.println("Market: " + instgroup.getMarket());
     	   if (instgroup.getMarket() == MarketType.SPOT.toString()){
     	   System.out.println("Market: " + instgroup.getBidPrice());
     	   }
     	   System.out.println("Instance type: " + instgroup.getInstanceType());
     	   System.out.println("Instances: " + instgroup.getRunningInstanceCount() +" / " +instgroup.getRequestedInstanceCount() );
     	    
        } 
	}
	
	
	//This method verifies if an instance group using Spot instances has less instances running than initially scheduled and 
	//launch a new instance group to replace previous one.
	public static void verifyHealthyRunningSpotInstances(AmazonElasticMapReduce client, String clusterId){
		int difference=0;
		File touchfile=null;
		
        List<InstanceGroup> instgrouplist = client.listInstanceGroups(new ListInstanceGroupsRequest().withClusterId(clusterId)).getInstanceGroups();
        for (InstanceGroup instgroup: instgrouplist ){
        	System.out.println("checking " + instgroup.getId().toString());
        	difference = getDiffRunningSpotInstances(client, clusterId, instgroup);
        	//difference=1; //forcing difference to create a new group.
        	System.out.println("difference: " + difference );
        	if (difference>0){
        		touchfile= new File("/tmp/"+ instgroup.getId() + ".txt"); //this file has the name of the instance group. If file already exists, the replacement for that group was already created. No need to launch a new instance group.
        		//if it is a TASK group AND are SPOT instances AND the group was not created already: 
        		if (instgroup.getInstanceGroupType().compareTo("TASK")==0 && instgroup.getMarket().compareTo(MarketType.SPOT.toString())==0 && !fileExist(touchfile) ){
            		System.out.println("Let's launch more instances " + "Market: " + instgroup.getMarket());
                    InstanceGroupConfig instanceGroupConfigTask = new InstanceGroupConfig(); //creating a new instanceGroup.
                    instanceGroupConfigTask.setInstanceCount(instgroup.getRequestedInstanceCount());
                    instanceGroupConfigTask.setInstanceRole("TASK");
                    instanceGroupConfigTask.setInstanceType(instgroup.getInstanceType());
                    instanceGroupConfigTask.setMarket("SPOT");
                    instanceGroupConfigTask.setName("AddedBy spot-health-checker - " + new Timestamp(new java.util.Date().getDate()));
                    Double bidPrice=Double.parseDouble(instgroup.getBidPrice()) + 0.01;
                    instanceGroupConfigTask.setBidPrice( String.format("%.2f",bidPrice) ); //need to increment by 1 cent.
                    try{ //write touch file. 
                    	FileUtils.touch(touchfile);	
                    }catch (Exception e) {
                        System.out.println("error trying to write touch file:" + e.getMessage());
                    }
                    System.out.println("creating request to add Instance Group");
            		AddInstanceGroupsRequest request=new AddInstanceGroupsRequest();
                    request.setJobFlowId(clusterId);
                    List<InstanceGroupConfig> list = new ArrayList<InstanceGroupConfig>(); //
                    list.add(instanceGroupConfigTask);
                    request.setInstanceGroups(list);
                    System.out.println("added request:" + request.toString());
                    client.addInstanceGroups(request);                   
        		}
        		//to evaluate: I cannot create a core group yet if it is lost by bid price. Cluster will terminate if core group is lost.
        	}else
        		System.out.println("no difference");

        } 
	}
	
	private static int getDiffRunningSpotInstances(AmazonElasticMapReduce client, String clusterId, InstanceGroup instgroup)
	{
		int count=0;
	    if (instgroup.getMarket().compareTo(MarketType.SPOT.toString())==0){
	       System.out.println("Market: " + instgroup.getBidPrice());
	       System.out.println("Instances: " + instgroup.getRunningInstanceCount() +" / " +instgroup.getRequestedInstanceCount() );
	       //I will return difference if Instance Group is not provisioning.
	       if (instgroup.getRunningInstanceCount() < instgroup.getRequestedInstanceCount()){// && instgroup.getStatus().getState().compareTo("PROVISIONING")==0 ){
	    	   count=(instgroup.getRequestedInstanceCount() - instgroup.getRunningInstanceCount());
	       }
	    } 
		return count;
	}
	


	private static boolean fileExist(File file) {
		System.out.println("checking if file exists: " + file.toString());
		if (file.exists()) {
			System.out.println(file.getName() + " exists");
			return true;
		} else {
			System.out.println(file.getName() + " does not exist");
			return false;
		}
	}
	
	private static String getClusterId() {
		String jobFlowId="";
		
		//String file="/tmp/extraInstanceData.json";
		String file="/mnt/var/lib/instance-controller/extraInstanceData.json";
		
			File f = new File(file);
	        if (f.exists()){
	            InputStream is;
				try {
					is = new FileInputStream(file);
		            String jsonTxt = IOUtils.toString(is);
		            //System.out.println(jsonTxt);
		            JSONObject json = new JSONObject(jsonTxt);       
		            jobFlowId = json.getString("jobFlowId");
		            System.out.println(jobFlowId);   
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        }
		return jobFlowId;
	}

}