package com.zycus.pm.script.utilities;

import java.util.HashMap;
import java.util.Map;

import com.zycus.pm.api.bo.IPermisibleResource;

public class ResourceUtility {

	private static Map<String, IPermisibleResource>	resources	= new HashMap<String, IPermisibleResource>();

	public static void addResource(final int resourceId, final String resourceName, final int resourceTypeId) {
		resources.put(resourceName, new IPermisibleResource() {

			public IPermisibleResource getParent() {
				return null;
			}

			public long getResourceID() {
				return resourceId;
			}

			public String getResourceName() {
				return resourceName;
			}

			public long getResourceTypeID() {
				return resourceTypeId;
			}

			public int compareTo(IPermisibleResource o) {
				return 0;
			}

		});
	}

	public static IPermisibleResource getResource(String resourceName) {
		return resources.get(resourceName);
	}
}
