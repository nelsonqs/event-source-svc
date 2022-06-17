package com.event.source.svc.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * CNCFCloud Event 
 * Refer to the cncf git url for reference and adhere to the cncf standard
 * https://github.com/cloudevents/spec/blob/v1.0/spec.json
 * https://github.com/cloudevents/spec/blob/v1.0/json-format.md
 * @author Ranjana
 *
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CNCFCloudEvent implements Serializable{

	private static final long serialVersionUID = -4657113530281341768L;
	private String specversion = "1.0.0";
	private String type;
	private String subject;
	private String source;
	private long time;
	private String datacontenttype;
	private Object data;
}
