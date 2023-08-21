package com.project.tedi.wrapper;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import com.project.tedi.model.Accomodation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@XmlRootElement(name = "accomodations")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccomodationListingWrapper {
    private List<Accomodation> accomodation;
}
