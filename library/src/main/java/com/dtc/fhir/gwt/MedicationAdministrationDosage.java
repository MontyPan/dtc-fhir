//
// 此檔案是由 JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 所產生 
// 請參閱 <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// 一旦重新編譯來源綱要, 對此檔案所做的任何修改都將會遺失. 
// 產生時間: 2016.08.31 於 11:09:06 PM CST 
//


package com.dtc.fhir.gwt;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * Describes the event of a patient consuming or otherwise being administered a medication.  This may be as simple as swallowing a tablet or it may be a long running infusion.  Related resources tie this event to the authorizing prescription, and the specific encounter between patient and health care practitioner.
 * 
 * <p>MedicationAdministration.Dosage complex type 的 Java 類別.
 * 
 * <p>下列綱要片段會指定此類別中包含的預期內容.
 * 
 * <pre>
 * &lt;complexType name="MedicationAdministration.Dosage">
 *   &lt;complexContent>
 *     &lt;extension base="{http://hl7.org/fhir}BackboneElement">
 *       &lt;sequence>
 *         &lt;element name="text" type="{http://hl7.org/fhir}string" minOccurs="0"/>
 *         &lt;choice minOccurs="0">
 *           &lt;element name="siteCodeableConcept" type="{http://hl7.org/fhir}CodeableConcept"/>
 *           &lt;element name="siteReference" type="{http://hl7.org/fhir}Reference"/>
 *         &lt;/choice>
 *         &lt;element name="route" type="{http://hl7.org/fhir}CodeableConcept" minOccurs="0"/>
 *         &lt;element name="method" type="{http://hl7.org/fhir}CodeableConcept" minOccurs="0"/>
 *         &lt;element name="quantity" type="{http://hl7.org/fhir}SimpleQuantity" minOccurs="0"/>
 *         &lt;choice minOccurs="0">
 *           &lt;element name="rateRatio" type="{http://hl7.org/fhir}Ratio"/>
 *           &lt;element name="rateRange" type="{http://hl7.org/fhir}Range"/>
 *         &lt;/choice>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MedicationAdministration.Dosage", propOrder = {
    "text",
    "siteCodeableConcept",
    "siteReference",
    "route",
    "method",
    "quantity",
    "rateRatio",
    "rateRange"
})
public class MedicationAdministrationDosage
    extends BackboneElement
{

    protected StringDt text;
    protected CodeableConcept siteCodeableConcept;
    protected Reference siteReference;
    protected CodeableConcept route;
    protected CodeableConcept method;
    protected SimpleQuantity quantity;
    protected Ratio rateRatio;
    protected Range rateRange;

    /**
     * 取得 text 特性的值.
     * 
     * @return
     *     possible object is
     *     {@link StringDt }
     *     
     */
    public StringDt getText() {
        return text;
    }

    /**
     * 設定 text 特性的值.
     * 
     * @param value
     *     allowed object is
     *     {@link StringDt }
     *     
     */
    public void setText(StringDt value) {
        this.text = value;
    }

    /**
     * 取得 siteCodeableConcept 特性的值.
     * 
     * @return
     *     possible object is
     *     {@link CodeableConcept }
     *     
     */
    public CodeableConcept getSiteCodeableConcept() {
        return siteCodeableConcept;
    }

    /**
     * 設定 siteCodeableConcept 特性的值.
     * 
     * @param value
     *     allowed object is
     *     {@link CodeableConcept }
     *     
     */
    public void setSiteCodeableConcept(CodeableConcept value) {
        this.siteCodeableConcept = value;
    }

    /**
     * 取得 siteReference 特性的值.
     * 
     * @return
     *     possible object is
     *     {@link Reference }
     *     
     */
    public Reference getSiteReference() {
        return siteReference;
    }

    /**
     * 設定 siteReference 特性的值.
     * 
     * @param value
     *     allowed object is
     *     {@link Reference }
     *     
     */
    public void setSiteReference(Reference value) {
        this.siteReference = value;
    }

    /**
     * 取得 route 特性的值.
     * 
     * @return
     *     possible object is
     *     {@link CodeableConcept }
     *     
     */
    public CodeableConcept getRoute() {
        return route;
    }

    /**
     * 設定 route 特性的值.
     * 
     * @param value
     *     allowed object is
     *     {@link CodeableConcept }
     *     
     */
    public void setRoute(CodeableConcept value) {
        this.route = value;
    }

    /**
     * 取得 method 特性的值.
     * 
     * @return
     *     possible object is
     *     {@link CodeableConcept }
     *     
     */
    public CodeableConcept getMethod() {
        return method;
    }

    /**
     * 設定 method 特性的值.
     * 
     * @param value
     *     allowed object is
     *     {@link CodeableConcept }
     *     
     */
    public void setMethod(CodeableConcept value) {
        this.method = value;
    }

    /**
     * 取得 quantity 特性的值.
     * 
     * @return
     *     possible object is
     *     {@link SimpleQuantity }
     *     
     */
    public SimpleQuantity getQuantity() {
        return quantity;
    }

    /**
     * 設定 quantity 特性的值.
     * 
     * @param value
     *     allowed object is
     *     {@link SimpleQuantity }
     *     
     */
    public void setQuantity(SimpleQuantity value) {
        this.quantity = value;
    }

    /**
     * 取得 rateRatio 特性的值.
     * 
     * @return
     *     possible object is
     *     {@link Ratio }
     *     
     */
    public Ratio getRateRatio() {
        return rateRatio;
    }

    /**
     * 設定 rateRatio 特性的值.
     * 
     * @param value
     *     allowed object is
     *     {@link Ratio }
     *     
     */
    public void setRateRatio(Ratio value) {
        this.rateRatio = value;
    }

    /**
     * 取得 rateRange 特性的值.
     * 
     * @return
     *     possible object is
     *     {@link Range }
     *     
     */
    public Range getRateRange() {
        return rateRange;
    }

    /**
     * 設定 rateRange 特性的值.
     * 
     * @param value
     *     allowed object is
     *     {@link Range }
     *     
     */
    public void setRateRange(Range value) {
        this.rateRange = value;
    }

}
