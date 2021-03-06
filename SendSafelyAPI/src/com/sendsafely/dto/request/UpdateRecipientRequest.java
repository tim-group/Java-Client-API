package com.sendsafely.dto.request;

import com.sendsafely.enums.CountryCode;
import com.sendsafely.enums.GetParam;
import com.sendsafely.enums.HTTPMethod;

public class UpdateRecipientRequest extends BaseRequest 
{	
	
	private HTTPMethod method = HTTPMethod.POST;
	private String path = "/package/" + GetParam.PACKAGE_ID + "/recipient/" + GetParam.RECIPIENT_ID + "/";
	
	public UpdateRecipientRequest() {
		initialize(method, path);
	}
	
	public void setPhonenumber(String email)
	{
		super.setPostParam("email", email);
	}
	
	public void setPackageId(String packageId)
	{
		super.setGetParam(GetParam.PACKAGE_ID, packageId);
	}
	
	public void setRecipientId(String recipientId)
	{
		super.setGetParam(GetParam.RECIPIENT_ID, recipientId);
	}
	
	public void addPhonenumber(String phonenumber, CountryCode countryCode)
	{
		super.setPostParam("phoneNumber", phonenumber);
		super.setPostParam("countrycode", countryCode);
	}
	
}
