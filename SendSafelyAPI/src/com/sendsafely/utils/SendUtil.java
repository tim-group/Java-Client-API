package com.sendsafely.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;

import com.sendsafely.dto.request.BaseRequest;
import com.sendsafely.dto.request.DownloadFileRequest;
import com.sendsafely.dto.request.UploadFileRequest;
import com.sendsafely.dto.response.DownloadFileResponse;
import com.sendsafely.dto.response.ResponseFactory;
import com.sendsafely.dto.response.UploadFileResponse;
import com.sendsafely.enums.APIResponse;
import com.sendsafely.enums.HTTPMethod;
import com.sendsafely.exceptions.SendFailedException;
import com.sendsafely.upload.UploadManager;

public class SendUtil {

	private UploadManager connection;
	
	public SendUtil(UploadManager uploadManager)
	{
		this.connection = uploadManager;
	}
	
	public <T> T send(String path, T returnObject, BaseRequest request) throws SendFailedException, IOException 
	{
		String data = (request.hasPostBody()) ? request.getPostBody() : "";		
		return send(path, request.getMethod(), data, returnObject);
	}
	
	public UploadFileResponse sendFile(String path, UploadFileRequest request, File file, String filename, Progress progress) throws SendFailedException, IOException
	{
		String data = request.getPostBody();
	
		String response = connection.sendFile(path, file, filename, data, progress);
		return ResponseFactory.getInstanceFromString(response, new UploadFileResponse());
	}
	
	protected <T> T send(String path, HTTPMethod method, String data, T clazz) throws IOException, SendFailedException
	{
		connection.send(path, method, data);
		return handleResponse(clazz);
	}
	
	protected <T> T handleResponse(T clazz) throws IOException, SendFailedException
	{
		if(connection.getContentType().equals("application/octet-stream")) {
			return handleFileDownload(clazz);
		} else {
			String response = connection.getResponse();
			return ResponseFactory.getInstanceFromString(response, clazz);
		}
	}
	
	protected <T> T handleFileDownload(T clazz) throws SendFailedException
	{
		InputStream stream = connection.getStream();
		
		if(!(clazz instanceof DownloadFileResponse))
		{
			throw new SendFailedException("File Download Responses must inherit from DownloadFileResponse");
		}
		
		DownloadFileResponse response = (DownloadFileResponse)clazz;
		response.setResponse(APIResponse.SUCCESS);
		response.setFileStream(stream);
		
		return clazz;
	}
	
}
