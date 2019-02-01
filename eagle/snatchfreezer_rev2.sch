<?xml version="1.0" encoding="utf-8"?>
<!DOCTYPE eagle SYSTEM "eagle.dtd">
<eagle version="7.1.0">
<drawing>
<settings>
<setting alwaysvectorfont="no"/>
<setting verticaltext="up"/>
</settings>
<grid distance="0.1" unitdist="inch" unit="inch" style="lines" multiple="1" display="no" altdistance="0.01" altunitdist="inch" altunit="inch"/>
<layers>
<layer number="1" name="Top" color="4" fill="1" visible="no" active="no"/>
<layer number="16" name="Bottom" color="1" fill="1" visible="no" active="no"/>
<layer number="17" name="Pads" color="2" fill="1" visible="no" active="no"/>
<layer number="18" name="Vias" color="2" fill="1" visible="no" active="no"/>
<layer number="19" name="Unrouted" color="6" fill="1" visible="no" active="no"/>
<layer number="20" name="Dimension" color="15" fill="1" visible="no" active="no"/>
<layer number="21" name="tPlace" color="7" fill="1" visible="no" active="no"/>
<layer number="22" name="bPlace" color="7" fill="1" visible="no" active="no"/>
<layer number="23" name="tOrigins" color="15" fill="1" visible="no" active="no"/>
<layer number="24" name="bOrigins" color="15" fill="1" visible="no" active="no"/>
<layer number="25" name="tNames" color="7" fill="1" visible="no" active="no"/>
<layer number="26" name="bNames" color="7" fill="1" visible="no" active="no"/>
<layer number="27" name="tValues" color="7" fill="1" visible="no" active="no"/>
<layer number="28" name="bValues" color="7" fill="1" visible="no" active="no"/>
<layer number="29" name="tStop" color="7" fill="3" visible="no" active="no"/>
<layer number="30" name="bStop" color="7" fill="6" visible="no" active="no"/>
<layer number="31" name="tCream" color="7" fill="4" visible="no" active="no"/>
<layer number="32" name="bCream" color="7" fill="5" visible="no" active="no"/>
<layer number="33" name="tFinish" color="6" fill="3" visible="no" active="no"/>
<layer number="34" name="bFinish" color="6" fill="6" visible="no" active="no"/>
<layer number="35" name="tGlue" color="7" fill="4" visible="no" active="no"/>
<layer number="36" name="bGlue" color="7" fill="5" visible="no" active="no"/>
<layer number="37" name="tTest" color="7" fill="1" visible="no" active="no"/>
<layer number="38" name="bTest" color="7" fill="1" visible="no" active="no"/>
<layer number="39" name="tKeepout" color="4" fill="11" visible="no" active="no"/>
<layer number="40" name="bKeepout" color="1" fill="11" visible="no" active="no"/>
<layer number="41" name="tRestrict" color="4" fill="10" visible="no" active="no"/>
<layer number="42" name="bRestrict" color="1" fill="10" visible="no" active="no"/>
<layer number="43" name="vRestrict" color="2" fill="10" visible="no" active="no"/>
<layer number="44" name="Drills" color="7" fill="1" visible="no" active="no"/>
<layer number="45" name="Holes" color="7" fill="1" visible="no" active="no"/>
<layer number="46" name="Milling" color="3" fill="1" visible="no" active="no"/>
<layer number="47" name="Measures" color="7" fill="1" visible="no" active="no"/>
<layer number="48" name="Document" color="7" fill="1" visible="no" active="no"/>
<layer number="49" name="Reference" color="7" fill="1" visible="no" active="no"/>
<layer number="51" name="tDocu" color="7" fill="1" visible="no" active="no"/>
<layer number="52" name="bDocu" color="7" fill="1" visible="no" active="no"/>
<layer number="91" name="Nets" color="2" fill="1" visible="yes" active="yes"/>
<layer number="92" name="Busses" color="1" fill="1" visible="yes" active="yes"/>
<layer number="93" name="Pins" color="2" fill="1" visible="no" active="yes"/>
<layer number="94" name="Symbols" color="4" fill="1" visible="yes" active="yes"/>
<layer number="95" name="Names" color="7" fill="1" visible="yes" active="yes"/>
<layer number="96" name="Values" color="7" fill="1" visible="yes" active="yes"/>
<layer number="97" name="Info" color="7" fill="1" visible="yes" active="yes"/>
<layer number="98" name="Guide" color="6" fill="1" visible="yes" active="yes"/>
</layers>
<schematic xreflabel="%F%N/%S.%C%R" xrefpart="/%S.%C%R">
<libraries>
<library name="drazil">
<packages>
<package name="ARDUINO_NANO30">
<pad name="1" x="-7.62" y="17.78" drill="0.6" diameter="1.9304" shape="long"/>
<pad name="2" x="-7.62" y="15.24" drill="0.6" diameter="1.9304" shape="long"/>
<pad name="3" x="-7.62" y="12.7" drill="0.6" diameter="1.9304" shape="long"/>
<pad name="4" x="-7.62" y="10.16" drill="0.6" diameter="1.9304" shape="long"/>
<pad name="5" x="-7.62" y="7.62" drill="0.6" diameter="1.9304" shape="long"/>
<pad name="6" x="-7.62" y="5.08" drill="0.6" diameter="1.9304" shape="long"/>
<pad name="7" x="-7.62" y="2.54" drill="0.6" diameter="1.9304" shape="long"/>
<pad name="8" x="-7.62" y="0" drill="0.6" diameter="1.9304" shape="long"/>
<pad name="9" x="-7.62" y="-2.54" drill="0.6" diameter="1.9304" shape="long"/>
<pad name="10" x="-7.62" y="-5.08" drill="0.6" diameter="1.9304" shape="long"/>
<pad name="11" x="-7.62" y="-7.62" drill="0.6" diameter="1.9304" shape="long"/>
<pad name="12" x="-7.62" y="-10.16" drill="0.6" diameter="1.9304" shape="long"/>
<pad name="13" x="-7.62" y="-12.7" drill="0.6" diameter="1.9304" shape="long"/>
<pad name="14" x="-7.62" y="-15.24" drill="0.6" diameter="1.9304" shape="long"/>
<pad name="15" x="-7.62" y="-17.78" drill="0.6" diameter="1.9304" shape="long"/>
<pad name="16" x="7.62" y="-17.78" drill="0.6" diameter="1.9304" shape="long"/>
<pad name="17" x="7.62" y="-15.24" drill="0.6" diameter="1.9304" shape="long"/>
<pad name="18" x="7.62" y="-12.7" drill="0.6" diameter="1.9304" shape="long"/>
<pad name="19" x="7.62" y="-10.16" drill="0.6" diameter="1.9304" shape="long"/>
<pad name="20" x="7.62" y="-7.62" drill="0.6" diameter="1.9304" shape="long"/>
<pad name="21" x="7.62" y="-5.08" drill="0.6" diameter="1.9304" shape="long"/>
<pad name="22" x="7.62" y="-2.54" drill="0.6" diameter="1.9304" shape="long"/>
<pad name="23" x="7.62" y="0" drill="0.6" diameter="1.9304" shape="long"/>
<pad name="24" x="7.62" y="2.54" drill="0.6" diameter="1.9304" shape="long"/>
<pad name="25" x="7.62" y="5.08" drill="0.6" diameter="1.9304" shape="long"/>
<pad name="26" x="7.62" y="7.62" drill="0.6" diameter="1.9304" shape="long"/>
<pad name="27" x="7.62" y="10.16" drill="0.6" diameter="1.9304" shape="long"/>
<pad name="28" x="7.62" y="12.7" drill="0.6" diameter="1.9304" shape="long"/>
<pad name="29" x="7.62" y="15.24" drill="0.6" diameter="1.9304" shape="long"/>
<pad name="30" x="7.62" y="17.78" drill="0.6" diameter="1.9304" shape="long"/>
<wire x1="10.16" y1="20.32" x2="10.16" y2="-20.32" width="1.016" layer="21"/>
<wire x1="10.16" y1="-20.32" x2="-10.16" y2="-20.32" width="1.016" layer="21"/>
<wire x1="-10.16" y1="-20.32" x2="-10.16" y2="20.32" width="1.016" layer="21"/>
<wire x1="-10.16" y1="20.32" x2="-5.08" y2="20.32" width="1.016" layer="21"/>
<wire x1="-5.08" y1="20.32" x2="-2.54" y2="17.78" width="1.016" layer="21"/>
<wire x1="-2.54" y1="17.78" x2="2.54" y2="17.78" width="1.016" layer="21"/>
<wire x1="2.54" y1="17.78" x2="5.08" y2="20.32" width="1.016" layer="21"/>
<wire x1="5.08" y1="20.32" x2="10.16" y2="20.32" width="1.016" layer="21"/>
<text x="5.08" y="-3.81" size="1.778" layer="21" rot="R90">ArduinoNanoV3.0</text>
<rectangle x1="-3.81" y1="-13.97" x2="3.81" y2="-6.35" layer="21" rot="R135"/>
</package>
<package name="IRLR8721">
<pad name="S" x="0" y="2.127" drill="0.8" diameter="1.778" shape="long"/>
<pad name="D" x="0" y="0" drill="0.8" diameter="1.778" shape="long"/>
<pad name="G" x="0" y="-2.127" drill="0.8" diameter="1.778" shape="long"/>
<wire x1="-2" y1="-4" x2="2" y2="-4" width="0.127" layer="21"/>
<wire x1="2" y1="-4" x2="2" y2="4" width="0.127" layer="21"/>
<wire x1="2" y1="4" x2="-2" y2="4" width="0.127" layer="21"/>
<wire x1="-2" y1="4" x2="-2" y2="-4" width="0.6096" layer="21"/>
</package>
<package name="RESISTOR">
<pad name="1" x="-7.62" y="0" drill="0.6" diameter="1.9304"/>
<pad name="2" x="5.08" y="0" drill="0.6" diameter="1.9304"/>
<wire x1="-5.08" y1="1.27" x2="2.54" y2="1.27" width="0.127" layer="21"/>
<wire x1="2.54" y1="1.27" x2="2.54" y2="0" width="0.127" layer="21"/>
<wire x1="2.54" y1="0" x2="2.54" y2="-1.27" width="0.127" layer="21"/>
<wire x1="2.54" y1="-1.27" x2="-5.08" y2="-1.27" width="0.127" layer="21"/>
<wire x1="-5.08" y1="-1.27" x2="-5.08" y2="0" width="0.127" layer="21"/>
<wire x1="-5.08" y1="0" x2="-5.08" y2="1.27" width="0.127" layer="21"/>
<wire x1="-5.08" y1="0" x2="-7.62" y2="0" width="0.127" layer="21"/>
<wire x1="2.54" y1="0" x2="5.08" y2="0" width="0.127" layer="21"/>
</package>
<package name="PHONEJACK_35">
<wire x1="-6.19" y1="6.54" x2="-2.19" y2="6.54" width="0.127" layer="21"/>
<wire x1="-2.19" y1="6.54" x2="1.81" y2="6.54" width="0.127" layer="21"/>
<wire x1="1.81" y1="6.54" x2="5.81" y2="6.54" width="0.127" layer="21"/>
<wire x1="5.81" y1="6.54" x2="5.81" y2="-6.46" width="0.127" layer="21"/>
<wire x1="-6.19" y1="-6.46" x2="-6.19" y2="6.54" width="0.127" layer="21"/>
<wire x1="-2.19" y1="6.54" x2="-2.19" y2="8.54" width="0.127" layer="21"/>
<wire x1="-2.19" y1="8.54" x2="1.81" y2="8.54" width="0.127" layer="21"/>
<wire x1="1.81" y1="8.54" x2="1.81" y2="6.54" width="0.127" layer="21"/>
<pad name="R" x="-5.19" y="-5.46" drill="0.6" diameter="2.54"/>
<pad name="B" x="-3.19" y="-3.46" drill="0.6" diameter="2.54" rot="R90"/>
<pad name="S" x="-3.19" y="3.54" drill="0.6" diameter="2.54"/>
<wire x1="-6.19" y1="-6.46" x2="5.81" y2="-6.46" width="0.127" layer="21"/>
<pad name="T" x="3.81" y="-4.46" drill="0.6" diameter="2.54"/>
</package>
<package name="LED3MM">
<pad name="A" x="-2.54" y="0" drill="0.6" diameter="1.9304"/>
<pad name="K" x="0" y="0" drill="0.6" diameter="1.9304"/>
<circle x="-1.27" y="0" radius="1.79605" width="0.127" layer="21"/>
</package>
<package name="DIODE">
<pad name="A" x="-5.08" y="0" drill="0.6" diameter="1.9304"/>
<pad name="K" x="5.08" y="0" drill="0.6" diameter="1.9304"/>
<wire x1="-5.08" y1="0" x2="1.27" y2="0" width="0.127" layer="21"/>
<wire x1="1.27" y1="0" x2="5.08" y2="0" width="0.127" layer="21"/>
<wire x1="1.27" y1="2.54" x2="1.27" y2="0" width="0.127" layer="21"/>
<wire x1="1.27" y1="0" x2="1.27" y2="-2.54" width="0.127" layer="21"/>
<wire x1="1.27" y1="0" x2="-1.27" y2="2.54" width="0.127" layer="21"/>
<wire x1="-1.27" y1="2.54" x2="-1.27" y2="-2.54" width="0.127" layer="21"/>
<wire x1="-1.27" y1="-2.54" x2="1.27" y2="0" width="0.127" layer="21"/>
</package>
<package name="POWER_CONNECT">
<pad name="2" x="0" y="-2.54" drill="0.6" diameter="3.81" rot="R90"/>
<pad name="1" x="0" y="3.81" drill="0.6" diameter="3.81"/>
<pad name="3" x="5.08" y="1.27" drill="0.6" diameter="3.81"/>
<wire x1="6.35" y1="5.08" x2="-3.81" y2="5.08" width="0.127" layer="21"/>
<wire x1="-3.81" y1="5.08" x2="-3.81" y2="-6.35" width="0.127" layer="21"/>
<wire x1="-3.81" y1="-6.35" x2="-1.27" y2="-6.35" width="0.127" layer="21"/>
<wire x1="-1.27" y1="-6.35" x2="-1.27" y2="-7.62" width="0.127" layer="21"/>
<wire x1="-1.27" y1="-7.62" x2="3.81" y2="-7.62" width="0.127" layer="21"/>
<wire x1="3.81" y1="-7.62" x2="3.81" y2="-6.35" width="0.127" layer="21"/>
<wire x1="3.81" y1="-6.35" x2="6.35" y2="-6.35" width="0.127" layer="21"/>
<wire x1="6.35" y1="-6.35" x2="6.35" y2="5.08" width="0.127" layer="21"/>
</package>
<package name="IC-16">
<pad name="8" x="-3.81" y="-8.89" drill="0.6" diameter="1.9304" shape="long"/>
<pad name="7" x="-3.81" y="-6.35" drill="0.6" diameter="1.9304" shape="long"/>
<pad name="6" x="-3.81" y="-3.81" drill="0.6" diameter="1.9304" shape="long"/>
<pad name="5" x="-3.81" y="-1.27" drill="0.6" diameter="1.9304" shape="long"/>
<pad name="4" x="-3.81" y="1.27" drill="0.6" diameter="1.9304" shape="long"/>
<pad name="3" x="-3.81" y="3.81" drill="0.6" diameter="1.9304" shape="long"/>
<pad name="2" x="-3.81" y="6.35" drill="0.6" diameter="1.9304" shape="long"/>
<pad name="1" x="-3.81" y="8.89" drill="0.6" diameter="1.9304" shape="long"/>
<pad name="16" x="3.81" y="8.89" drill="0.6" diameter="1.9304" shape="long"/>
<pad name="15" x="3.81" y="6.35" drill="0.6" diameter="1.9304" shape="long"/>
<pad name="14" x="3.81" y="3.81" drill="0.6" diameter="1.9304" shape="long"/>
<pad name="13" x="3.81" y="1.27" drill="0.6" diameter="1.9304" shape="long"/>
<pad name="12" x="3.81" y="-1.27" drill="0.6" diameter="1.9304" shape="long"/>
<pad name="11" x="3.81" y="-3.81" drill="0.6" diameter="1.9304" shape="long"/>
<pad name="10" x="3.81" y="-6.35" drill="0.6" diameter="1.9304" shape="long"/>
<pad name="9" x="3.81" y="-8.89" drill="0.6" diameter="1.9304" shape="long"/>
<wire x1="-5.08" y1="10.16" x2="-5.08" y2="-10.16" width="0.127" layer="21"/>
<wire x1="-5.08" y1="-10.16" x2="5.08" y2="-10.16" width="0.127" layer="21"/>
<wire x1="5.08" y1="-10.16" x2="5.08" y2="10.16" width="0.127" layer="21"/>
<wire x1="5.08" y1="10.16" x2="2.54" y2="10.16" width="0.127" layer="21"/>
<wire x1="2.54" y1="10.16" x2="2.54" y2="8.89" width="0.127" layer="21"/>
<wire x1="2.54" y1="8.89" x2="-2.54" y2="8.89" width="0.127" layer="21"/>
<wire x1="-2.54" y1="8.89" x2="-2.54" y2="10.16" width="0.127" layer="21"/>
<wire x1="-2.54" y1="10.16" x2="-5.08" y2="10.16" width="0.127" layer="21"/>
</package>
<package name="PHONEJACK_25">
<wire x1="-2.46" y1="5" x2="-1.46" y2="5" width="0.127" layer="21"/>
<wire x1="-1.46" y1="5" x2="1.54" y2="5" width="0.127" layer="21"/>
<wire x1="1.54" y1="5" x2="2.54" y2="5" width="0.127" layer="21"/>
<wire x1="2.54" y1="5" x2="2.54" y2="-5" width="0.127" layer="21"/>
<wire x1="2.54" y1="-5" x2="-2.46" y2="-5" width="0.127" layer="21"/>
<wire x1="-2.46" y1="-5" x2="-2.46" y2="5" width="0.127" layer="21"/>
<wire x1="-1.46" y1="5" x2="-1.46" y2="6" width="0.127" layer="21"/>
<wire x1="-1.46" y1="6" x2="1.54" y2="6" width="0.127" layer="21"/>
<wire x1="1.54" y1="6" x2="1.54" y2="5" width="0.127" layer="21"/>
<pad name="2" x="0.04" y="-5" drill="0.6" diameter="2.54"/>
<pad name="1" x="-1.46" y="-2" drill="0.6" diameter="2.54"/>
<pad name="3" x="1.54" y="-2" drill="0.6" diameter="2.54"/>
</package>
<package name="736880-49">
<description>&lt;b&gt;Cinch Einbauprintbuchse&lt;/b&gt;, ( RCA Jack ) RM 7.5 x 10 mm&lt;p&gt;
Artikel-Nr.: 736880 - 49&lt;br&gt;
Source: http://www.produktinfo.conrad.com/datenblaetter/725000-749999/736880-da-01-de-Cinch-Einbaubuchse.pdf</description>
<wire x1="-4.92" y1="3" x2="-4.92" y2="-3" width="0.4096" layer="51"/>
<wire x1="-4.92" y1="5.5" x2="-4.92" y2="4.25" width="0.4096" layer="21"/>
<wire x1="-4.92" y1="4.25" x2="-4.92" y2="3" width="0.4096" layer="21"/>
<wire x1="-4.92" y1="-5.5" x2="-4.92" y2="-4" width="0.4096" layer="21"/>
<wire x1="-4.92" y1="-4" x2="-4.92" y2="-3" width="0.4096" layer="21"/>
<wire x1="-5.42" y1="-3.5" x2="-11.92" y2="-3.5" width="0.4096" layer="21"/>
<wire x1="-11.92" y1="-3.5" x2="-12.42" y2="-3" width="0.4096" layer="21" curve="-90"/>
<wire x1="-12.42" y1="-3" x2="-12.42" y2="3.25" width="0.4096" layer="21"/>
<wire x1="-12.42" y1="3.25" x2="-11.92" y2="3.75" width="0.4096" layer="21" curve="-90"/>
<wire x1="-11.92" y1="3.75" x2="-5.42" y2="3.75" width="0.4096" layer="21"/>
<wire x1="-4.92" y1="4.25" x2="-5.42" y2="3.75" width="0.4096" layer="21" curve="-90"/>
<wire x1="-5.42" y1="-3.5" x2="-4.92" y2="-4" width="0.4096" layer="21" curve="-90"/>
<wire x1="3.975" y1="6" x2="6.275" y2="6" width="0" layer="46"/>
<wire x1="6.275" y1="6" x2="6.275" y2="5" width="0" layer="46"/>
<wire x1="6.275" y1="5" x2="3.975" y2="5" width="0" layer="46"/>
<wire x1="3.975" y1="5" x2="3.975" y2="6" width="0" layer="46"/>
<wire x1="3.975" y1="-5" x2="6.275" y2="-5" width="0" layer="46"/>
<wire x1="6.275" y1="-5" x2="6.275" y2="-6" width="0" layer="46"/>
<wire x1="6.275" y1="-6" x2="3.975" y2="-6" width="0" layer="46"/>
<wire x1="3.975" y1="-6" x2="3.975" y2="-5" width="0" layer="46"/>
<wire x1="-5.025" y1="0.5" x2="-2.725" y2="0.5" width="0" layer="46"/>
<wire x1="-2.725" y1="0.5" x2="-2.725" y2="-0.5" width="0" layer="46"/>
<wire x1="-2.725" y1="-0.5" x2="-5.025" y2="-0.5" width="0" layer="46"/>
<wire x1="-5.025" y1="-0.5" x2="-5.025" y2="0.5" width="0" layer="46"/>
<pad name="P$1" x="-3.81" y="0.08" drill="1" diameter="2.54" rot="R270"/>
<pad name="SIGNAL" x="3.4" y="0" drill="1" diameter="3.81"/>
<pad name="P$5" x="5.8" y="-5.63" drill="1" diameter="3.048" shape="long"/>
<pad name="GND" x="5.63" y="5.63" drill="1" diameter="3.048" shape="long" rot="R180"/>
<wire x1="5.8" y1="5.8" x2="-4.8" y2="5.8" width="0.4064" layer="21"/>
<wire x1="5.8" y1="-5.8" x2="-4.8" y2="-5.8" width="0.4064" layer="21"/>
</package>
</packages>
<symbols>
<symbol name="ARDUINO_NANO30">
<wire x1="-25.4" y1="45.72" x2="27.94" y2="45.72" width="0.254" layer="94"/>
<wire x1="27.94" y1="45.72" x2="27.94" y2="-60.96" width="0.254" layer="94"/>
<wire x1="27.94" y1="-60.96" x2="-25.4" y2="-60.96" width="0.254" layer="94"/>
<wire x1="-25.4" y1="-60.96" x2="-25.4" y2="45.72" width="0.254" layer="94"/>
<pin name="A0" x="-33.02" y="38.1"/>
<pin name="A1" x="-33.02" y="30.48"/>
<pin name="A2" x="-33.02" y="22.86"/>
<pin name="A3" x="-33.02" y="15.24"/>
<pin name="A4" x="-33.02" y="7.62"/>
<pin name="A5" x="-33.02" y="0"/>
<pin name="A6" x="-33.02" y="-7.62"/>
<pin name="A7" x="-33.02" y="-15.24"/>
<pin name="D0/TX" x="35.56" y="38.1" rot="R180"/>
<pin name="D1/RX" x="35.56" y="33.02" rot="R180"/>
<pin name="D2" x="35.56" y="27.94" rot="R180"/>
<pin name="D3" x="35.56" y="22.86" rot="R180"/>
<pin name="D4" x="35.56" y="17.78" rot="R180"/>
<pin name="D5" x="35.56" y="12.7" rot="R180"/>
<pin name="D6" x="35.56" y="7.62" rot="R180"/>
<pin name="D7" x="35.56" y="2.54" rot="R180"/>
<pin name="D8" x="35.56" y="-2.54" rot="R180"/>
<pin name="D9" x="35.56" y="-7.62" rot="R180"/>
<pin name="D10" x="35.56" y="-12.7" rot="R180"/>
<pin name="D11" x="35.56" y="-17.78" rot="R180"/>
<pin name="D12" x="35.56" y="-22.86" rot="R180"/>
<pin name="GND" x="0" y="-68.58" rot="R90"/>
<pin name="RESET" x="15.24" y="-68.58" rot="R90"/>
<pin name="D13" x="35.56" y="-27.94" rot="R180"/>
<pin name="VIN" x="-15.24" y="53.34" rot="R270"/>
<pin name="5V" x="0" y="53.34" rot="R270"/>
<pin name="3.3V" x="15.24" y="53.34" rot="R270"/>
<pin name="REF" x="-15.24" y="-68.58" rot="R90"/>
<text x="5.08" y="-40.64" size="1.778" layer="95">Arduino Nano V3.0</text>
</symbol>
<symbol name="MOSFET">
<pin name="G" x="-10.16" y="-5.08" length="short"/>
<pin name="S" x="2.54" y="-10.16" length="short" rot="R90"/>
<pin name="D" x="2.54" y="7.62" length="short" rot="R270"/>
<wire x1="-7.62" y1="-5.08" x2="-5.08" y2="-5.08" width="0.254" layer="94"/>
<wire x1="-5.08" y1="-5.08" x2="-5.08" y2="2.54" width="0.254" layer="94"/>
<wire x1="-3.81" y1="3.81" x2="-3.81" y2="2.54" width="0.254" layer="94"/>
<wire x1="-3.81" y1="2.54" x2="-3.81" y2="1.27" width="0.254" layer="94"/>
<wire x1="-3.81" y1="0" x2="-3.81" y2="-1.27" width="0.254" layer="94"/>
<wire x1="-3.81" y1="-1.27" x2="-3.81" y2="-2.54" width="0.254" layer="94"/>
<wire x1="-3.81" y1="-3.81" x2="-3.81" y2="-5.08" width="0.254" layer="94"/>
<wire x1="-3.81" y1="-5.08" x2="-3.81" y2="-6.35" width="0.254" layer="94"/>
<wire x1="-3.81" y1="2.54" x2="2.54" y2="2.54" width="0.254" layer="94"/>
<wire x1="-3.81" y1="-1.27" x2="0" y2="-1.27" width="0.254" layer="94"/>
<wire x1="2.54" y1="2.54" x2="2.54" y2="5.08" width="0.254" layer="94"/>
<wire x1="2.54" y1="2.54" x2="2.54" y2="0" width="0.254" layer="94"/>
<wire x1="2.54" y1="0" x2="2.54" y2="-5.08" width="0.254" layer="94"/>
<wire x1="2.54" y1="-5.08" x2="2.54" y2="-7.62" width="0.254" layer="94"/>
<wire x1="-3.81" y1="-5.08" x2="0" y2="-5.08" width="0.254" layer="94"/>
<wire x1="0" y1="-5.08" x2="2.54" y2="-5.08" width="0.254" layer="94"/>
<wire x1="0" y1="-1.27" x2="0" y2="-5.08" width="0.254" layer="94"/>
<wire x1="-2.54" y1="0" x2="-3.81" y2="-1.27" width="0.254" layer="94"/>
<wire x1="-3.81" y1="-1.27" x2="-2.54" y2="-2.54" width="0.254" layer="94"/>
<wire x1="2.54" y1="0" x2="3.81" y2="-2.54" width="0.254" layer="94"/>
<wire x1="3.81" y1="-2.54" x2="1.27" y2="-2.54" width="0.254" layer="94"/>
<wire x1="1.27" y1="-2.54" x2="2.54" y2="0" width="0.254" layer="94"/>
<wire x1="2.54" y1="0" x2="1.27" y2="0" width="0.254" layer="94"/>
<wire x1="2.54" y1="0" x2="3.81" y2="0" width="0.254" layer="94"/>
<circle x="-1.27" y="-1.27" radius="7.184203125" width="0.254" layer="94"/>
</symbol>
<symbol name="RESISTOR">
<pin name="1" x="-10.16" y="0" length="short"/>
<pin name="2" x="7.62" y="0" length="short" rot="R180"/>
<wire x1="-7.62" y1="2.54" x2="5.08" y2="2.54" width="0.254" layer="94"/>
<wire x1="5.08" y1="2.54" x2="5.08" y2="-2.54" width="0.254" layer="94"/>
<wire x1="5.08" y1="-2.54" x2="-7.62" y2="-2.54" width="0.254" layer="94"/>
<wire x1="-7.62" y1="-2.54" x2="-7.62" y2="2.54" width="0.254" layer="94"/>
</symbol>
<symbol name="PHONEJACK_35">
<pin name="T" x="12.7" y="2.54" length="short" rot="R180"/>
<pin name="B" x="12.7" y="0" length="short" rot="R180"/>
<pin name="R" x="12.7" y="-2.54" length="short" rot="R180"/>
<pin name="S" x="12.7" y="-5.08" length="short" rot="R180"/>
<wire x1="10.16" y1="2.54" x2="2.54" y2="2.54" width="0.254" layer="94"/>
<wire x1="2.54" y1="2.54" x2="0" y2="0" width="0.254" layer="94"/>
<wire x1="0" y1="0" x2="-2.54" y2="2.54" width="0.254" layer="94"/>
<wire x1="10.16" y1="-2.54" x2="5.08" y2="-2.54" width="0.254" layer="94"/>
<wire x1="5.08" y1="-2.54" x2="0" y2="-2.54" width="0.254" layer="94"/>
<wire x1="0" y1="-2.54" x2="-2.54" y2="0" width="0.254" layer="94"/>
<wire x1="-2.54" y1="0" x2="-5.08" y2="-2.54" width="0.254" layer="94"/>
<wire x1="10.16" y1="0" x2="5.08" y2="0" width="0.254" layer="94"/>
<wire x1="5.08" y1="0" x2="5.08" y2="-2.54" width="0.254" layer="94"/>
<wire x1="10.16" y1="-5.08" x2="-5.08" y2="-5.08" width="0.254" layer="94"/>
<wire x1="-5.08" y1="-5.08" x2="-7.62" y2="-2.54" width="0.254" layer="94"/>
<wire x1="-7.62" y1="-2.54" x2="-10.16" y2="-5.08" width="0.254" layer="94"/>
<wire x1="3.81" y1="-1.27" x2="5.08" y2="-2.54" width="0.254" layer="94"/>
<wire x1="5.08" y1="-2.54" x2="6.35" y2="-1.27" width="0.254" layer="94"/>
<wire x1="-12.7" y1="-5.08" x2="-12.7" y2="2.54" width="0.254" layer="94"/>
<wire x1="-12.7" y1="2.54" x2="-15.24" y2="2.54" width="0.254" layer="94"/>
<wire x1="-15.24" y1="2.54" x2="-15.24" y2="-5.08" width="0.254" layer="94"/>
<wire x1="-15.24" y1="-5.08" x2="-12.7" y2="-5.08" width="0.254" layer="94"/>
</symbol>
<symbol name="LED">
<wire x1="-7.62" y1="0" x2="2.54" y2="0" width="0.254" layer="94"/>
<wire x1="2.54" y1="0" x2="7.62" y2="0" width="0.254" layer="94"/>
<wire x1="-2.54" y1="2.54" x2="-2.54" y2="-2.54" width="0.254" layer="94"/>
<wire x1="-2.54" y1="-2.54" x2="2.54" y2="0" width="0.254" layer="94"/>
<wire x1="2.54" y1="0" x2="-2.54" y2="2.54" width="0.254" layer="94"/>
<wire x1="2.54" y1="2.54" x2="2.54" y2="-2.54" width="0.254" layer="94"/>
<wire x1="-0.7" y1="3.4" x2="0.7" y2="5.5" width="0.254" layer="94"/>
<wire x1="0.7" y1="5.5" x2="0.9" y2="4.7" width="0.254" layer="94"/>
<wire x1="0.7" y1="5.4" x2="-0.2" y2="5.4" width="0.254" layer="94"/>
<wire x1="1.3" y1="2.7" x2="2.4" y2="4.5" width="0.254" layer="94"/>
<wire x1="2.4" y1="4.5" x2="2.7" y2="3.6" width="0.254" layer="94"/>
<wire x1="2.4" y1="4.5" x2="1.4" y2="4.3" width="0.254" layer="94"/>
<pin name="A" x="-10.16" y="0" length="short"/>
<pin name="K" x="10.16" y="0" length="short" rot="R180"/>
</symbol>
<symbol name="DIODE">
<wire x1="-7.62" y1="0" x2="2.54" y2="0" width="0.254" layer="94"/>
<wire x1="2.54" y1="0" x2="7.62" y2="0" width="0.254" layer="94"/>
<wire x1="2.54" y1="0" x2="-2.54" y2="2.54" width="0.254" layer="94"/>
<wire x1="-2.54" y1="2.54" x2="-2.54" y2="-2.54" width="0.254" layer="94"/>
<wire x1="-2.54" y1="-2.54" x2="2.54" y2="0" width="0.254" layer="94"/>
<wire x1="2.54" y1="0" x2="2.54" y2="2.54" width="0.254" layer="94"/>
<wire x1="2.54" y1="0" x2="2.54" y2="-2.54" width="0.254" layer="94"/>
<pin name="A" x="-10.16" y="0" length="short"/>
<pin name="K" x="10.16" y="0" length="short" rot="R180"/>
</symbol>
<symbol name="POWER_CONNECT">
<pin name="VIN" x="-10.16" y="0" length="short"/>
<pin name="GND" x="7.62" y="0" length="short" rot="R180"/>
<circle x="-5.08" y="0" radius="2.54" width="0.6096" layer="94"/>
<circle x="2.54" y="0" radius="2.54" width="0.6096" layer="94"/>
<text x="-7.62" y="2.54" size="1.27" layer="94">+</text>
<text x="5.08" y="2.54" size="1.27" layer="94">-</text>
</symbol>
<symbol name="IC-16">
<pin name="1" x="-10.16" y="17.78" length="short"/>
<pin name="2" x="-10.16" y="12.7" length="short"/>
<pin name="3" x="-10.16" y="7.62" length="short"/>
<pin name="4" x="-10.16" y="2.54" length="short"/>
<pin name="5" x="-10.16" y="-2.54" length="short"/>
<pin name="6" x="-10.16" y="-7.62" length="short"/>
<pin name="7" x="-10.16" y="-12.7" length="short"/>
<pin name="8" x="-10.16" y="-17.78" length="short"/>
<pin name="9" x="12.7" y="-17.78" length="short" rot="R180"/>
<pin name="10" x="12.7" y="-12.7" length="short" rot="R180"/>
<pin name="11" x="12.7" y="-7.62" length="short" rot="R180"/>
<pin name="12" x="12.7" y="-2.54" length="short" rot="R180"/>
<pin name="13" x="12.7" y="2.54" length="short" rot="R180"/>
<pin name="14" x="12.7" y="7.62" length="short" rot="R180"/>
<pin name="15" x="12.7" y="12.7" length="short" rot="R180"/>
<pin name="16" x="12.7" y="17.78" length="short" rot="R180"/>
<wire x1="-7.62" y1="20.32" x2="10.16" y2="20.32" width="0.254" layer="94"/>
<wire x1="10.16" y1="20.32" x2="10.16" y2="-20.32" width="0.254" layer="94"/>
<wire x1="-7.62" y1="-20.32" x2="-7.62" y2="20.32" width="0.254" layer="94"/>
<wire x1="-7.62" y1="-20.32" x2="10.16" y2="-20.32" width="0.254" layer="94"/>
</symbol>
<symbol name="PHONEJACK_25">
<pin name="2" x="10.16" y="2.54" length="short" rot="R180"/>
<pin name="3" x="10.16" y="-2.54" length="short" rot="R180"/>
<pin name="1" x="10.16" y="-5.08" length="short" rot="R180"/>
<wire x1="7.62" y1="2.54" x2="2.54" y2="2.54" width="0.254" layer="94"/>
<wire x1="2.54" y1="2.54" x2="0" y2="0" width="0.254" layer="94"/>
<wire x1="0" y1="0" x2="-2.54" y2="2.54" width="0.254" layer="94"/>
<wire x1="7.62" y1="-2.54" x2="0" y2="-2.54" width="0.254" layer="94"/>
<wire x1="0" y1="-2.54" x2="-2.54" y2="0" width="0.254" layer="94"/>
<wire x1="-2.54" y1="0" x2="-5.08" y2="-2.54" width="0.254" layer="94"/>
<wire x1="7.62" y1="-5.08" x2="-5.08" y2="-5.08" width="0.254" layer="94"/>
<wire x1="-5.08" y1="-5.08" x2="-7.62" y2="-2.54" width="0.254" layer="94"/>
<wire x1="-7.62" y1="-2.54" x2="-10.16" y2="-5.08" width="0.254" layer="94"/>
<wire x1="-12.7" y1="-5.08" x2="-12.7" y2="2.54" width="0.254" layer="94"/>
<wire x1="-12.7" y1="2.54" x2="-15.24" y2="2.54" width="0.254" layer="94"/>
<wire x1="-15.24" y1="2.54" x2="-15.24" y2="-5.08" width="0.254" layer="94"/>
<wire x1="-15.24" y1="-5.08" x2="-12.7" y2="-5.08" width="0.254" layer="94"/>
</symbol>
<symbol name="CINCH">
<text x="-5.08" y="3.81" size="1.778" layer="95">&gt;NAME</text>
<text x="-5.08" y="-6.35" size="1.778" layer="96">&gt;VALUE</text>
<rectangle x1="-5.08" y1="-0.508" x2="-2.54" y2="0.508" layer="94"/>
<pin name="2" x="2.54" y="0" visible="off" length="middle" direction="pas" rot="R180"/>
<pin name="1" x="2.54" y="-2.54" visible="off" length="middle" direction="pas" rot="R180"/>
<wire x1="-5.08" y1="-2.54" x2="-3.175" y2="-2.54" width="0.254" layer="94"/>
<wire x1="-3.175" y1="-2.54" x2="-1.016" y2="-0.381" width="0.254" layer="94" curve="90"/>
<wire x1="-5.08" y1="2.54" x2="-3.175" y2="2.54" width="0.254" layer="94"/>
<wire x1="-3.175" y1="2.54" x2="-1.016" y2="0.381" width="0.254" layer="94" curve="-90"/>
</symbol>
</symbols>
<devicesets>
<deviceset name="ARDUINO_NANO30">
<gates>
<gate name="G$1" symbol="ARDUINO_NANO30" x="10.16" y="7.62"/>
</gates>
<devices>
<device name="" package="ARDUINO_NANO30">
<connects>
<connect gate="G$1" pin="3.3V" pad="17"/>
<connect gate="G$1" pin="5V" pad="27"/>
<connect gate="G$1" pin="A0" pad="19"/>
<connect gate="G$1" pin="A1" pad="20"/>
<connect gate="G$1" pin="A2" pad="21"/>
<connect gate="G$1" pin="A3" pad="22"/>
<connect gate="G$1" pin="A4" pad="23"/>
<connect gate="G$1" pin="A5" pad="24"/>
<connect gate="G$1" pin="A6" pad="25"/>
<connect gate="G$1" pin="A7" pad="26"/>
<connect gate="G$1" pin="D0/TX" pad="1"/>
<connect gate="G$1" pin="D1/RX" pad="2"/>
<connect gate="G$1" pin="D10" pad="13"/>
<connect gate="G$1" pin="D11" pad="14"/>
<connect gate="G$1" pin="D12" pad="15"/>
<connect gate="G$1" pin="D13" pad="16"/>
<connect gate="G$1" pin="D2" pad="5"/>
<connect gate="G$1" pin="D3" pad="6"/>
<connect gate="G$1" pin="D4" pad="7"/>
<connect gate="G$1" pin="D5" pad="8"/>
<connect gate="G$1" pin="D6" pad="9"/>
<connect gate="G$1" pin="D7" pad="10"/>
<connect gate="G$1" pin="D8" pad="11"/>
<connect gate="G$1" pin="D9" pad="12"/>
<connect gate="G$1" pin="GND" pad="4 29"/>
<connect gate="G$1" pin="REF" pad="18"/>
<connect gate="G$1" pin="RESET" pad="3 28"/>
<connect gate="G$1" pin="VIN" pad="30"/>
</connects>
<technologies>
<technology name=""/>
</technologies>
</device>
</devices>
</deviceset>
<deviceset name="IRLR8721">
<gates>
<gate name="G$1" symbol="MOSFET" x="2.54" y="0"/>
</gates>
<devices>
<device name="" package="IRLR8721">
<connects>
<connect gate="G$1" pin="D" pad="D"/>
<connect gate="G$1" pin="G" pad="G"/>
<connect gate="G$1" pin="S" pad="S"/>
</connects>
<technologies>
<technology name=""/>
</technologies>
</device>
</devices>
</deviceset>
<deviceset name="RESISTOR">
<gates>
<gate name="G$1" symbol="RESISTOR" x="-5.08" y="0"/>
</gates>
<devices>
<device name="" package="RESISTOR">
<connects>
<connect gate="G$1" pin="1" pad="1"/>
<connect gate="G$1" pin="2" pad="2"/>
</connects>
<technologies>
<technology name=""/>
</technologies>
</device>
</devices>
</deviceset>
<deviceset name="PHONEJACK_35">
<gates>
<gate name="G$1" symbol="PHONEJACK_35" x="-7.62" y="2.54"/>
</gates>
<devices>
<device name="" package="PHONEJACK_35">
<connects>
<connect gate="G$1" pin="B" pad="B"/>
<connect gate="G$1" pin="R" pad="R"/>
<connect gate="G$1" pin="S" pad="S"/>
<connect gate="G$1" pin="T" pad="T"/>
</connects>
<technologies>
<technology name=""/>
</technologies>
</device>
</devices>
</deviceset>
<deviceset name="LED3MM">
<gates>
<gate name="G$1" symbol="LED" x="-5.08" y="0"/>
</gates>
<devices>
<device name="" package="LED3MM">
<connects>
<connect gate="G$1" pin="A" pad="A"/>
<connect gate="G$1" pin="K" pad="K"/>
</connects>
<technologies>
<technology name=""/>
</technologies>
</device>
</devices>
</deviceset>
<deviceset name="DIODE">
<gates>
<gate name="G$1" symbol="DIODE" x="-35.56" y="15.24"/>
</gates>
<devices>
<device name="" package="DIODE">
<connects>
<connect gate="G$1" pin="A" pad="A"/>
<connect gate="G$1" pin="K" pad="K"/>
</connects>
<technologies>
<technology name=""/>
</technologies>
</device>
</devices>
</deviceset>
<deviceset name="POWER_CONNECT">
<gates>
<gate name="G$1" symbol="POWER_CONNECT" x="0" y="0"/>
</gates>
<devices>
<device name="" package="POWER_CONNECT">
<connects>
<connect gate="G$1" pin="GND" pad="2 3"/>
<connect gate="G$1" pin="VIN" pad="1"/>
</connects>
<technologies>
<technology name=""/>
</technologies>
</device>
</devices>
</deviceset>
<deviceset name="IC-16">
<gates>
<gate name="G$1" symbol="IC-16" x="2.54" y="-2.54"/>
</gates>
<devices>
<device name="" package="IC-16">
<connects>
<connect gate="G$1" pin="1" pad="1"/>
<connect gate="G$1" pin="10" pad="10"/>
<connect gate="G$1" pin="11" pad="11"/>
<connect gate="G$1" pin="12" pad="12"/>
<connect gate="G$1" pin="13" pad="13"/>
<connect gate="G$1" pin="14" pad="14"/>
<connect gate="G$1" pin="15" pad="15"/>
<connect gate="G$1" pin="16" pad="16"/>
<connect gate="G$1" pin="2" pad="2"/>
<connect gate="G$1" pin="3" pad="3"/>
<connect gate="G$1" pin="4" pad="4"/>
<connect gate="G$1" pin="5" pad="5"/>
<connect gate="G$1" pin="6" pad="6"/>
<connect gate="G$1" pin="7" pad="7"/>
<connect gate="G$1" pin="8" pad="8"/>
<connect gate="G$1" pin="9" pad="9"/>
</connects>
<technologies>
<technology name=""/>
</technologies>
</device>
</devices>
</deviceset>
<deviceset name="PHONEJACK_25">
<gates>
<gate name="G$1" symbol="PHONEJACK_25" x="0" y="7.62"/>
</gates>
<devices>
<device name="" package="PHONEJACK_25">
<connects>
<connect gate="G$1" pin="1" pad="1"/>
<connect gate="G$1" pin="2" pad="2"/>
<connect gate="G$1" pin="3" pad="3"/>
</connects>
<technologies>
<technology name=""/>
</technologies>
</device>
</devices>
</deviceset>
<deviceset name="736880-49" prefix="X">
<description>&lt;b&gt;Cinch Connector&lt;/b&gt; ( RCA Jack )&lt;p&gt;
Cinch-Einbaukupplung 90º 72345 Vernickelt BKL Electronic&lt;br&gt;
Source: &lt;a href="http://www.conrad.de/ce/de/product/736880"&gt; Conrad &lt;/a&gt;</description>
<gates>
<gate name="G$1" symbol="CINCH" x="0" y="0"/>
</gates>
<devices>
<device name="" package="736880-49">
<connects>
<connect gate="G$1" pin="1" pad="GND"/>
<connect gate="G$1" pin="2" pad="SIGNAL"/>
</connects>
<technologies>
<technology name=""/>
</technologies>
</device>
</devices>
</deviceset>
</devicesets>
</library>
</libraries>
<attributes>
</attributes>
<variantdefs>
</variantdefs>
<classes>
<class number="0" name="default" width="0" drill="0">
</class>
</classes>
<parts>
<part name="U$1" library="drazil" deviceset="ARDUINO_NANO30" device=""/>
<part name="U$2" library="drazil" deviceset="IRLR8721" device="">
<attribute name="MF1" value=""/>
</part>
<part name="R1" library="drazil" deviceset="RESISTOR" device="">
<attribute name="R1" value="220"/>
</part>
<part name="U$6" library="drazil" deviceset="PHONEJACK_35" device=""/>
<part name="U$7" library="drazil" deviceset="PHONEJACK_35" device=""/>
<part name="U$11" library="drazil" deviceset="LED3MM" device="">
<attribute name="LED1" value=""/>
</part>
<part name="U$12" library="drazil" deviceset="DIODE" device="">
<attribute name="D1" value=""/>
</part>
<part name="R2" library="drazil" deviceset="RESISTOR" device="">
<attribute name="R2" value=""/>
</part>
<part name="R3" library="drazil" deviceset="RESISTOR" device="">
<attribute name="R3" value=""/>
</part>
<part name="U$3" library="drazil" deviceset="IRLR8721" device="">
<attribute name="MF2" value=""/>
</part>
<part name="R4" library="drazil" deviceset="RESISTOR" device="">
<attribute name="R4" value=""/>
</part>
<part name="U$10" library="drazil" deviceset="LED3MM" device="">
<attribute name="LED2" value=""/>
</part>
<part name="U$13" library="drazil" deviceset="DIODE" device="">
<attribute name="D2" value=""/>
</part>
<part name="R5" library="drazil" deviceset="RESISTOR" device="">
<attribute name="R5" value=""/>
</part>
<part name="R6" library="drazil" deviceset="RESISTOR" device="">
<attribute name="R6" value=""/>
</part>
<part name="U$20" library="drazil" deviceset="IRLR8721" device="">
<attribute name="MF3" value=""/>
</part>
<part name="R7" library="drazil" deviceset="RESISTOR" device="">
<attribute name="R7" value=""/>
</part>
<part name="U$23" library="drazil" deviceset="LED3MM" device="">
<attribute name="LED3" value=""/>
</part>
<part name="U$24" library="drazil" deviceset="DIODE" device="">
<attribute name="D3" value=""/>
</part>
<part name="R8" library="drazil" deviceset="RESISTOR" device="">
<attribute name="R8" value="220"/>
</part>
<part name="R9" library="drazil" deviceset="RESISTOR" device="">
<attribute name="R9" value=""/>
</part>
<part name="U$28" library="drazil" deviceset="IRLR8721" device="">
<attribute name="MF4" value=""/>
</part>
<part name="R10" library="drazil" deviceset="RESISTOR" device="">
<attribute name="R10" value=""/>
</part>
<part name="U$31" library="drazil" deviceset="LED3MM" device="">
<attribute name="LED4" value=""/>
</part>
<part name="U$32" library="drazil" deviceset="DIODE" device="">
<attribute name="D4" value=""/>
</part>
<part name="R11" library="drazil" deviceset="RESISTOR" device="">
<attribute name="R11" value=""/>
</part>
<part name="R12" library="drazil" deviceset="RESISTOR" device="">
<attribute name="R12" value=""/>
</part>
<part name="U$36" library="drazil" deviceset="POWER_CONNECT" device=""/>
<part name="U$8" library="drazil" deviceset="IC-16" device=""/>
<part name="U$30" library="drazil" deviceset="RESISTOR" device="">
<attribute name="R16" value=""/>
</part>
<part name="U$37" library="drazil" deviceset="RESISTOR" device="">
<attribute name="R15" value=""/>
</part>
<part name="U$38" library="drazil" deviceset="RESISTOR" device="">
<attribute name="R14" value=""/>
</part>
<part name="U$39" library="drazil" deviceset="RESISTOR" device="">
<attribute name="R13" value=""/>
</part>
<part name="U$40" library="drazil" deviceset="PHONEJACK_25" device=""/>
<part name="U$41" library="drazil" deviceset="PHONEJACK_25" device=""/>
<part name="U$9" library="drazil" deviceset="RESISTOR" device="">
<attribute name="R17" value=""/>
</part>
<part name="U$22" library="drazil" deviceset="LED3MM" device="">
<attribute name="LED5" value=""/>
</part>
<part name="U$4" library="drazil" deviceset="IRLR8721" device="">
<attribute name="MF4" value=""/>
</part>
<part name="R13" library="drazil" deviceset="RESISTOR" device="">
<attribute name="R13" value=""/>
</part>
<part name="U$5" library="drazil" deviceset="LED3MM" device="">
<attribute name="LED4" value=""/>
</part>
<part name="U$14" library="drazil" deviceset="DIODE" device="">
<attribute name="D4" value=""/>
</part>
<part name="R14" library="drazil" deviceset="RESISTOR" device="">
<attribute name="R14" value=""/>
</part>
<part name="R15" library="drazil" deviceset="RESISTOR" device="">
<attribute name="R15" value=""/>
</part>
<part name="U$16" library="drazil" deviceset="IRLR8721" device="">
<attribute name="MF4" value=""/>
</part>
<part name="R16" library="drazil" deviceset="RESISTOR" device="">
<attribute name="R16" value=""/>
</part>
<part name="U$17" library="drazil" deviceset="LED3MM" device="">
<attribute name="LED4" value=""/>
</part>
<part name="U$21" library="drazil" deviceset="DIODE" device="">
<attribute name="D4" value=""/>
</part>
<part name="R17" library="drazil" deviceset="RESISTOR" device="">
<attribute name="R17" value=""/>
</part>
<part name="R18" library="drazil" deviceset="RESISTOR" device="">
<attribute name="R18" value=""/>
</part>
<part name="X1" library="drazil" deviceset="736880-49" device=""/>
<part name="X2" library="drazil" deviceset="736880-49" device=""/>
<part name="X3" library="drazil" deviceset="736880-49" device=""/>
<part name="X4" library="drazil" deviceset="736880-49" device=""/>
<part name="X5" library="drazil" deviceset="736880-49" device=""/>
<part name="X6" library="drazil" deviceset="736880-49" device=""/>
</parts>
<sheets>
<sheet>
<plain>
</plain>
<instances>
<instance part="U$1" gate="G$1" x="22.86" y="53.34"/>
<instance part="U$2" gate="G$1" x="309.88" y="154.94">
<attribute name="MF1" x="309.88" y="154.94" size="1.778" layer="96" display="both"/>
</instance>
<instance part="R1" gate="G$1" x="236.22" y="149.86">
<attribute name="R1" x="236.22" y="149.86" size="1.778" layer="95" display="both"/>
</instance>
<instance part="U$6" gate="G$1" x="271.78" y="-185.42" rot="MR0"/>
<instance part="U$7" gate="G$1" x="271.78" y="-172.72" rot="MR0"/>
<instance part="U$11" gate="G$1" x="264.16" y="127">
<attribute name="LED1" x="264.16" y="127" size="1.778" layer="96" display="both"/>
</instance>
<instance part="U$12" gate="G$1" x="337.82" y="152.4" rot="R90">
<attribute name="D1" x="337.82" y="152.4" size="1.778" layer="96" rot="R90" display="both"/>
</instance>
<instance part="R2" gate="G$1" x="236.22" y="127">
<attribute name="R2" x="236.22" y="127" size="1.778" layer="96" display="both"/>
</instance>
<instance part="R3" gate="G$1" x="284.48" y="139.7" rot="R90">
<attribute name="R3" x="284.48" y="139.7" size="1.778" layer="96" rot="R90" display="name"/>
</instance>
<instance part="U$3" gate="G$1" x="309.88" y="106.68">
<attribute name="MF2" x="309.88" y="106.68" size="1.778" layer="96" display="both"/>
</instance>
<instance part="R4" gate="G$1" x="236.22" y="101.6">
<attribute name="R4" x="236.22" y="101.6" size="1.778" layer="96" display="both"/>
</instance>
<instance part="U$10" gate="G$1" x="264.16" y="78.74">
<attribute name="LED2" x="264.16" y="78.74" size="1.778" layer="96" display="both"/>
</instance>
<instance part="U$13" gate="G$1" x="337.82" y="104.14" rot="R90">
<attribute name="D2" x="337.82" y="104.14" size="1.778" layer="96" rot="R90" display="both"/>
</instance>
<instance part="R5" gate="G$1" x="236.22" y="78.74">
<attribute name="R5" x="236.22" y="78.74" size="1.778" layer="96" display="both"/>
</instance>
<instance part="R6" gate="G$1" x="284.48" y="91.44" rot="R90">
<attribute name="R6" x="284.48" y="91.44" size="1.778" layer="96" rot="R90" display="both"/>
</instance>
<instance part="U$20" gate="G$1" x="309.88" y="58.42">
<attribute name="MF3" x="309.88" y="58.42" size="1.778" layer="96" display="both"/>
</instance>
<instance part="R7" gate="G$1" x="236.22" y="53.34">
<attribute name="R7" x="236.22" y="53.34" size="1.778" layer="96" display="both"/>
</instance>
<instance part="U$23" gate="G$1" x="264.16" y="30.48">
<attribute name="LED3" x="264.16" y="30.48" size="1.778" layer="96" display="both"/>
</instance>
<instance part="U$24" gate="G$1" x="337.82" y="55.88" rot="R90">
<attribute name="D3" x="337.82" y="55.88" size="1.778" layer="96" rot="R90" display="off"/>
</instance>
<instance part="R8" gate="G$1" x="236.22" y="30.48">
<attribute name="R8" x="236.22" y="30.48" size="1.778" layer="96" display="both"/>
</instance>
<instance part="R9" gate="G$1" x="284.48" y="43.18" rot="R90">
<attribute name="R9" x="284.48" y="43.18" size="1.778" layer="96" rot="R90" display="both"/>
</instance>
<instance part="U$28" gate="G$1" x="309.88" y="10.16">
<attribute name="MF4" x="309.88" y="10.16" size="1.778" layer="96" display="both"/>
</instance>
<instance part="R10" gate="G$1" x="236.22" y="5.08">
<attribute name="R10" x="236.22" y="5.08" size="1.778" layer="96" display="both"/>
</instance>
<instance part="U$31" gate="G$1" x="264.16" y="-17.78">
<attribute name="LED4" x="264.16" y="-17.78" size="1.778" layer="96" display="both"/>
</instance>
<instance part="U$32" gate="G$1" x="337.82" y="7.62" rot="R90">
<attribute name="D4" x="337.82" y="7.62" size="1.778" layer="96" rot="R90" display="both"/>
</instance>
<instance part="R11" gate="G$1" x="236.22" y="-17.78">
<attribute name="R11" x="236.22" y="-17.78" size="1.778" layer="96" display="both"/>
</instance>
<instance part="R12" gate="G$1" x="284.48" y="-5.08" rot="R90">
<attribute name="R12" x="284.48" y="-5.08" size="1.778" layer="96" rot="R90" display="both"/>
</instance>
<instance part="U$36" gate="G$1" x="-30.48" y="170.18" rot="MR0"/>
<instance part="U$8" gate="G$1" x="215.9" y="-187.96"/>
<instance part="U$30" gate="G$1" x="106.68" y="-147.32" rot="R90">
<attribute name="R16" x="106.68" y="-147.32" size="1.778" layer="96" rot="R90" display="both"/>
</instance>
<instance part="U$37" gate="G$1" x="96.52" y="-147.32" rot="R90">
<attribute name="R15" x="96.52" y="-147.32" size="1.778" layer="96" rot="R90" display="both"/>
</instance>
<instance part="U$38" gate="G$1" x="86.36" y="-147.32" rot="R90">
<attribute name="R14" x="86.36" y="-147.32" size="1.778" layer="96" rot="R90" display="both"/>
</instance>
<instance part="U$39" gate="G$1" x="76.2" y="-147.32" rot="R90">
<attribute name="R13" x="76.2" y="-147.32" size="1.778" layer="96" rot="R90" display="both"/>
</instance>
<instance part="U$40" gate="G$1" x="271.78" y="-200.66" rot="R180"/>
<instance part="U$41" gate="G$1" x="271.78" y="-213.36" rot="R180"/>
<instance part="U$9" gate="G$1" x="68.58" y="12.7" rot="R90">
<attribute name="R17" x="68.58" y="12.7" size="1.778" layer="96" rot="R90" display="both"/>
</instance>
<instance part="U$22" gate="G$1" x="68.58" y="-12.7" rot="R270">
<attribute name="LED5" x="68.58" y="-12.7" size="1.778" layer="96" rot="R270" display="both"/>
</instance>
<instance part="U$4" gate="G$1" x="309.88" y="-35.56">
<attribute name="MF4" x="309.88" y="-35.56" size="1.778" layer="96" display="both"/>
</instance>
<instance part="R13" gate="G$1" x="236.22" y="-40.64">
<attribute name="R13" x="236.22" y="-40.64" size="1.778" layer="96" display="both"/>
</instance>
<instance part="U$5" gate="G$1" x="264.16" y="-63.5">
<attribute name="LED4" x="264.16" y="-63.5" size="1.778" layer="96" display="both"/>
</instance>
<instance part="U$14" gate="G$1" x="337.82" y="-38.1" rot="R90">
<attribute name="D4" x="337.82" y="-38.1" size="1.778" layer="96" rot="R90" display="both"/>
</instance>
<instance part="R14" gate="G$1" x="236.22" y="-63.5">
<attribute name="R14" x="236.22" y="-63.5" size="1.778" layer="96" display="both"/>
</instance>
<instance part="R15" gate="G$1" x="284.48" y="-50.8" rot="R90">
<attribute name="R15" x="284.48" y="-50.8" size="1.778" layer="96" rot="R90" display="both"/>
</instance>
<instance part="U$16" gate="G$1" x="309.88" y="-81.28">
<attribute name="MF4" x="309.88" y="-81.28" size="1.778" layer="96" display="both"/>
</instance>
<instance part="R16" gate="G$1" x="236.22" y="-86.36">
<attribute name="R16" x="236.22" y="-86.36" size="1.778" layer="96" display="both"/>
</instance>
<instance part="U$17" gate="G$1" x="264.16" y="-109.22">
<attribute name="LED4" x="264.16" y="-109.22" size="1.778" layer="96" display="both"/>
</instance>
<instance part="U$21" gate="G$1" x="337.82" y="-83.82" rot="R90">
<attribute name="D4" x="337.82" y="-83.82" size="1.778" layer="96" rot="R90" display="both"/>
</instance>
<instance part="R17" gate="G$1" x="236.22" y="-109.22">
<attribute name="R17" x="236.22" y="-109.22" size="1.778" layer="96" display="both"/>
</instance>
<instance part="R18" gate="G$1" x="284.48" y="-96.52" rot="R90">
<attribute name="R18" x="284.48" y="-96.52" size="1.778" layer="96" rot="R90" display="both"/>
</instance>
<instance part="X1" gate="G$1" x="360.68" y="149.86" rot="R180"/>
<instance part="X2" gate="G$1" x="360.68" y="101.6" rot="R180"/>
<instance part="X3" gate="G$1" x="360.68" y="53.34" rot="R180"/>
<instance part="X4" gate="G$1" x="360.68" y="5.08" rot="R180"/>
<instance part="X5" gate="G$1" x="360.68" y="-40.64" rot="R180"/>
<instance part="X6" gate="G$1" x="360.68" y="-86.36" rot="R180"/>
</instances>
<busses>
</busses>
<nets>
<net name="N$2" class="0">
<segment>
<pinref part="U$12" gate="G$1" pin="A"/>
<wire x1="337.82" y1="142.24" x2="337.82" y2="139.7" width="0.1524" layer="91"/>
<pinref part="U$2" gate="G$1" pin="D"/>
<wire x1="312.42" y1="162.56" x2="322.58" y2="162.56" width="0.1524" layer="91"/>
<wire x1="322.58" y1="162.56" x2="322.58" y2="139.7" width="0.1524" layer="91"/>
<wire x1="322.58" y1="139.7" x2="337.82" y2="139.7" width="0.1524" layer="91"/>
<wire x1="337.82" y1="139.7" x2="342.9" y2="139.7" width="0.1524" layer="91"/>
<wire x1="342.9" y1="139.7" x2="353.06" y2="149.86" width="0.1524" layer="91"/>
<pinref part="X1" gate="G$1" pin="2"/>
<wire x1="353.06" y1="149.86" x2="358.14" y2="149.86" width="0.1524" layer="91"/>
</segment>
</net>
<net name="N$3" class="0">
<segment>
<pinref part="R2" gate="G$1" pin="2"/>
<pinref part="U$11" gate="G$1" pin="A"/>
<wire x1="243.84" y1="127" x2="254" y2="127" width="0.1524" layer="91"/>
</segment>
</net>
<net name="N$1" class="0">
<segment>
<pinref part="R1" gate="G$1" pin="2"/>
<pinref part="U$2" gate="G$1" pin="G"/>
<wire x1="243.84" y1="149.86" x2="284.48" y2="149.86" width="0.1524" layer="91"/>
<pinref part="R3" gate="G$1" pin="2"/>
<wire x1="284.48" y1="149.86" x2="299.72" y2="149.86" width="0.1524" layer="91"/>
<wire x1="284.48" y1="147.32" x2="284.48" y2="149.86" width="0.1524" layer="91"/>
<junction x="284.48" y="149.86"/>
</segment>
</net>
<net name="N$6" class="0">
<segment>
<pinref part="R2" gate="G$1" pin="1"/>
<wire x1="226.06" y1="127" x2="215.9" y2="127" width="0.1524" layer="91"/>
<pinref part="R1" gate="G$1" pin="1"/>
<wire x1="226.06" y1="149.86" x2="215.9" y2="149.86" width="0.1524" layer="91"/>
<wire x1="215.9" y1="127" x2="215.9" y2="149.86" width="0.1524" layer="91"/>
<wire x1="215.9" y1="149.86" x2="177.8" y2="149.86" width="0.1524" layer="91"/>
<wire x1="177.8" y1="149.86" x2="172.72" y2="144.78" width="0.1524" layer="91"/>
<wire x1="172.72" y1="144.78" x2="172.72" y2="86.36" width="0.1524" layer="91"/>
<wire x1="172.72" y1="86.36" x2="167.64" y2="81.28" width="0.1524" layer="91"/>
<pinref part="U$1" gate="G$1" pin="D2"/>
<wire x1="167.64" y1="81.28" x2="58.42" y2="81.28" width="0.1524" layer="91"/>
<junction x="215.9" y="149.86"/>
</segment>
</net>
<net name="N$7" class="0">
<segment>
<pinref part="U$13" gate="G$1" pin="A"/>
<wire x1="337.82" y1="93.98" x2="337.82" y2="91.44" width="0.1524" layer="91"/>
<pinref part="U$3" gate="G$1" pin="D"/>
<wire x1="312.42" y1="114.3" x2="322.58" y2="114.3" width="0.1524" layer="91"/>
<wire x1="322.58" y1="114.3" x2="322.58" y2="91.44" width="0.1524" layer="91"/>
<wire x1="322.58" y1="91.44" x2="337.82" y2="91.44" width="0.1524" layer="91"/>
<wire x1="337.82" y1="91.44" x2="342.9" y2="91.44" width="0.1524" layer="91"/>
<wire x1="342.9" y1="91.44" x2="353.06" y2="101.6" width="0.1524" layer="91"/>
<pinref part="X2" gate="G$1" pin="2"/>
<wire x1="353.06" y1="101.6" x2="358.14" y2="101.6" width="0.1524" layer="91"/>
</segment>
</net>
<net name="N$10" class="0">
<segment>
<pinref part="R5" gate="G$1" pin="2"/>
<pinref part="U$10" gate="G$1" pin="A"/>
<wire x1="243.84" y1="78.74" x2="254" y2="78.74" width="0.1524" layer="91"/>
</segment>
</net>
<net name="N$11" class="0">
<segment>
<pinref part="R4" gate="G$1" pin="2"/>
<pinref part="U$3" gate="G$1" pin="G"/>
<wire x1="243.84" y1="101.6" x2="284.48" y2="101.6" width="0.1524" layer="91"/>
<pinref part="R6" gate="G$1" pin="2"/>
<wire x1="284.48" y1="101.6" x2="299.72" y2="101.6" width="0.1524" layer="91"/>
<wire x1="284.48" y1="99.06" x2="284.48" y2="101.6" width="0.1524" layer="91"/>
<junction x="284.48" y="101.6"/>
</segment>
</net>
<net name="N$12" class="0">
<segment>
<pinref part="R5" gate="G$1" pin="1"/>
<wire x1="226.06" y1="78.74" x2="215.9" y2="78.74" width="0.1524" layer="91"/>
<pinref part="R4" gate="G$1" pin="1"/>
<wire x1="226.06" y1="101.6" x2="215.9" y2="101.6" width="0.1524" layer="91"/>
<wire x1="215.9" y1="78.74" x2="215.9" y2="101.6" width="0.1524" layer="91"/>
<wire x1="215.9" y1="101.6" x2="180.34" y2="101.6" width="0.1524" layer="91"/>
<wire x1="180.34" y1="101.6" x2="177.8" y2="99.06" width="0.1524" layer="91"/>
<wire x1="177.8" y1="99.06" x2="177.8" y2="83.82" width="0.1524" layer="91"/>
<wire x1="177.8" y1="83.82" x2="170.18" y2="76.2" width="0.1524" layer="91"/>
<pinref part="U$1" gate="G$1" pin="D3"/>
<wire x1="170.18" y1="76.2" x2="58.42" y2="76.2" width="0.1524" layer="91"/>
<junction x="215.9" y="101.6"/>
</segment>
</net>
<net name="N$13" class="0">
<segment>
<pinref part="U$24" gate="G$1" pin="A"/>
<wire x1="337.82" y1="45.72" x2="337.82" y2="43.18" width="0.1524" layer="91"/>
<pinref part="U$20" gate="G$1" pin="D"/>
<wire x1="312.42" y1="66.04" x2="322.58" y2="66.04" width="0.1524" layer="91"/>
<wire x1="322.58" y1="66.04" x2="322.58" y2="43.18" width="0.1524" layer="91"/>
<wire x1="322.58" y1="43.18" x2="337.82" y2="43.18" width="0.1524" layer="91"/>
<wire x1="337.82" y1="43.18" x2="342.9" y2="43.18" width="0.1524" layer="91"/>
<wire x1="342.9" y1="43.18" x2="353.06" y2="53.34" width="0.1524" layer="91"/>
<pinref part="X3" gate="G$1" pin="2"/>
<wire x1="353.06" y1="53.34" x2="358.14" y2="53.34" width="0.1524" layer="91"/>
</segment>
</net>
<net name="N$16" class="0">
<segment>
<pinref part="R8" gate="G$1" pin="2"/>
<pinref part="U$23" gate="G$1" pin="A"/>
<wire x1="243.84" y1="30.48" x2="254" y2="30.48" width="0.1524" layer="91"/>
</segment>
</net>
<net name="N$17" class="0">
<segment>
<pinref part="R7" gate="G$1" pin="2"/>
<pinref part="U$20" gate="G$1" pin="G"/>
<wire x1="243.84" y1="53.34" x2="284.48" y2="53.34" width="0.1524" layer="91"/>
<pinref part="R9" gate="G$1" pin="2"/>
<wire x1="284.48" y1="53.34" x2="299.72" y2="53.34" width="0.1524" layer="91"/>
<wire x1="284.48" y1="50.8" x2="284.48" y2="53.34" width="0.1524" layer="91"/>
<junction x="284.48" y="53.34"/>
</segment>
</net>
<net name="N$18" class="0">
<segment>
<pinref part="R8" gate="G$1" pin="1"/>
<wire x1="226.06" y1="30.48" x2="215.9" y2="30.48" width="0.1524" layer="91"/>
<pinref part="R7" gate="G$1" pin="1"/>
<wire x1="226.06" y1="53.34" x2="215.9" y2="53.34" width="0.1524" layer="91"/>
<wire x1="215.9" y1="30.48" x2="215.9" y2="53.34" width="0.1524" layer="91"/>
<wire x1="215.9" y1="53.34" x2="182.88" y2="53.34" width="0.1524" layer="91"/>
<wire x1="182.88" y1="53.34" x2="180.34" y2="55.88" width="0.1524" layer="91"/>
<wire x1="180.34" y1="55.88" x2="180.34" y2="66.04" width="0.1524" layer="91"/>
<wire x1="180.34" y1="66.04" x2="175.26" y2="71.12" width="0.1524" layer="91"/>
<pinref part="U$1" gate="G$1" pin="D4"/>
<wire x1="175.26" y1="71.12" x2="58.42" y2="71.12" width="0.1524" layer="91"/>
<junction x="215.9" y="53.34"/>
</segment>
</net>
<net name="N$19" class="0">
<segment>
<pinref part="U$32" gate="G$1" pin="A"/>
<wire x1="337.82" y1="-2.54" x2="337.82" y2="-5.08" width="0.1524" layer="91"/>
<pinref part="U$28" gate="G$1" pin="D"/>
<wire x1="312.42" y1="17.78" x2="322.58" y2="17.78" width="0.1524" layer="91"/>
<wire x1="322.58" y1="17.78" x2="322.58" y2="-5.08" width="0.1524" layer="91"/>
<wire x1="322.58" y1="-5.08" x2="337.82" y2="-5.08" width="0.1524" layer="91"/>
<wire x1="337.82" y1="-5.08" x2="342.9" y2="-5.08" width="0.1524" layer="91"/>
<wire x1="342.9" y1="-5.08" x2="353.06" y2="5.08" width="0.1524" layer="91"/>
<pinref part="X4" gate="G$1" pin="2"/>
<wire x1="353.06" y1="5.08" x2="358.14" y2="5.08" width="0.1524" layer="91"/>
</segment>
</net>
<net name="N$22" class="0">
<segment>
<pinref part="R11" gate="G$1" pin="2"/>
<pinref part="U$31" gate="G$1" pin="A"/>
<wire x1="243.84" y1="-17.78" x2="254" y2="-17.78" width="0.1524" layer="91"/>
</segment>
</net>
<net name="N$23" class="0">
<segment>
<pinref part="R10" gate="G$1" pin="2"/>
<pinref part="U$28" gate="G$1" pin="G"/>
<wire x1="243.84" y1="5.08" x2="284.48" y2="5.08" width="0.1524" layer="91"/>
<pinref part="R12" gate="G$1" pin="2"/>
<wire x1="284.48" y1="5.08" x2="299.72" y2="5.08" width="0.1524" layer="91"/>
<wire x1="284.48" y1="2.54" x2="284.48" y2="5.08" width="0.1524" layer="91"/>
<junction x="284.48" y="5.08"/>
</segment>
</net>
<net name="N$24" class="0">
<segment>
<pinref part="R11" gate="G$1" pin="1"/>
<wire x1="226.06" y1="-17.78" x2="215.9" y2="-17.78" width="0.1524" layer="91"/>
<pinref part="R10" gate="G$1" pin="1"/>
<wire x1="226.06" y1="5.08" x2="215.9" y2="5.08" width="0.1524" layer="91"/>
<wire x1="215.9" y1="-17.78" x2="215.9" y2="5.08" width="0.1524" layer="91"/>
<wire x1="215.9" y1="5.08" x2="177.8" y2="5.08" width="0.1524" layer="91"/>
<wire x1="177.8" y1="5.08" x2="172.72" y2="10.16" width="0.1524" layer="91"/>
<wire x1="172.72" y1="10.16" x2="172.72" y2="63.5" width="0.1524" layer="91"/>
<wire x1="172.72" y1="63.5" x2="170.18" y2="66.04" width="0.1524" layer="91"/>
<pinref part="U$1" gate="G$1" pin="D5"/>
<wire x1="170.18" y1="66.04" x2="58.42" y2="66.04" width="0.1524" layer="91"/>
<junction x="215.9" y="5.08"/>
</segment>
</net>
<net name="N$31" class="0">
<segment>
<pinref part="U$8" gate="G$1" pin="12"/>
<wire x1="228.6" y1="-190.5" x2="241.3" y2="-190.5" width="0.1524" layer="91"/>
<wire x1="241.3" y1="-190.5" x2="246.38" y2="-195.58" width="0.1524" layer="91"/>
<pinref part="U$40" gate="G$1" pin="1"/>
<wire x1="246.38" y1="-195.58" x2="261.62" y2="-195.58" width="0.1524" layer="91"/>
</segment>
</net>
<net name="N$32" class="0">
<segment>
<pinref part="U$8" gate="G$1" pin="11"/>
<wire x1="228.6" y1="-195.58" x2="238.76" y2="-195.58" width="0.1524" layer="91"/>
<wire x1="238.76" y1="-195.58" x2="246.38" y2="-203.2" width="0.1524" layer="91"/>
<wire x1="246.38" y1="-203.2" x2="251.46" y2="-203.2" width="0.1524" layer="91"/>
<pinref part="U$40" gate="G$1" pin="2"/>
<wire x1="251.46" y1="-203.2" x2="261.62" y2="-203.2" width="0.1524" layer="91"/>
<wire x1="251.46" y1="-203.2" x2="251.46" y2="-200.66" width="0.1524" layer="91"/>
<wire x1="251.46" y1="-200.66" x2="254" y2="-198.12" width="0.1524" layer="91"/>
<pinref part="U$40" gate="G$1" pin="3"/>
<wire x1="254" y1="-198.12" x2="261.62" y2="-198.12" width="0.1524" layer="91"/>
<junction x="251.46" y="-203.2"/>
</segment>
</net>
<net name="N$33" class="0">
<segment>
<pinref part="U$8" gate="G$1" pin="10"/>
<wire x1="228.6" y1="-200.66" x2="236.22" y2="-200.66" width="0.1524" layer="91"/>
<wire x1="236.22" y1="-200.66" x2="243.84" y2="-208.28" width="0.1524" layer="91"/>
<wire x1="243.84" y1="-208.28" x2="261.62" y2="-208.28" width="0.1524" layer="91"/>
<pinref part="U$41" gate="G$1" pin="1"/>
</segment>
</net>
<net name="N$34" class="0">
<segment>
<pinref part="U$8" gate="G$1" pin="9"/>
<wire x1="228.6" y1="-205.74" x2="233.68" y2="-205.74" width="0.1524" layer="91"/>
<wire x1="233.68" y1="-205.74" x2="243.84" y2="-215.9" width="0.1524" layer="91"/>
<wire x1="243.84" y1="-215.9" x2="251.46" y2="-215.9" width="0.1524" layer="91"/>
<pinref part="U$41" gate="G$1" pin="2"/>
<wire x1="251.46" y1="-215.9" x2="261.62" y2="-215.9" width="0.1524" layer="91"/>
<wire x1="251.46" y1="-215.9" x2="251.46" y2="-213.36" width="0.1524" layer="91"/>
<wire x1="251.46" y1="-213.36" x2="254" y2="-210.82" width="0.1524" layer="91"/>
<pinref part="U$41" gate="G$1" pin="3"/>
<wire x1="254" y1="-210.82" x2="261.62" y2="-210.82" width="0.1524" layer="91"/>
<junction x="251.46" y="-215.9"/>
</segment>
</net>
<net name="N$35" class="0">
<segment>
<pinref part="U$1" gate="G$1" pin="D13"/>
<wire x1="58.42" y1="25.4" x2="66.04" y2="25.4" width="0.1524" layer="91"/>
<wire x1="66.04" y1="25.4" x2="68.58" y2="22.86" width="0.1524" layer="91"/>
<pinref part="U$9" gate="G$1" pin="2"/>
<wire x1="68.58" y1="22.86" x2="68.58" y2="20.32" width="0.1524" layer="91"/>
</segment>
</net>
<net name="N$36" class="0">
<segment>
<pinref part="U$9" gate="G$1" pin="1"/>
<pinref part="U$22" gate="G$1" pin="A"/>
<wire x1="68.58" y1="2.54" x2="68.58" y2="-2.54" width="0.1524" layer="91"/>
</segment>
</net>
<net name="N$4" class="0">
<segment>
<pinref part="U$8" gate="G$1" pin="16"/>
<wire x1="228.6" y1="-170.18" x2="243.84" y2="-170.18" width="0.1524" layer="91"/>
<wire x1="243.84" y1="-170.18" x2="251.46" y2="-177.8" width="0.1524" layer="91"/>
<pinref part="U$7" gate="G$1" pin="S"/>
<wire x1="251.46" y1="-177.8" x2="259.08" y2="-177.8" width="0.1524" layer="91"/>
</segment>
</net>
<net name="N$9" class="0">
<segment>
<pinref part="U$8" gate="G$1" pin="15"/>
<wire x1="228.6" y1="-175.26" x2="246.38" y2="-175.26" width="0.1524" layer="91"/>
<wire x1="246.38" y1="-175.26" x2="251.46" y2="-170.18" width="0.1524" layer="91"/>
<pinref part="U$7" gate="G$1" pin="T"/>
<wire x1="251.46" y1="-170.18" x2="259.08" y2="-170.18" width="0.1524" layer="91"/>
</segment>
</net>
<net name="N$27" class="0">
<segment>
<pinref part="U$8" gate="G$1" pin="13"/>
<wire x1="228.6" y1="-185.42" x2="248.92" y2="-185.42" width="0.1524" layer="91"/>
<wire x1="248.92" y1="-185.42" x2="251.46" y2="-182.88" width="0.1524" layer="91"/>
<pinref part="U$6" gate="G$1" pin="T"/>
<wire x1="251.46" y1="-182.88" x2="259.08" y2="-182.88" width="0.1524" layer="91"/>
</segment>
</net>
<net name="N$28" class="0">
<segment>
<pinref part="U$8" gate="G$1" pin="14"/>
<wire x1="228.6" y1="-180.34" x2="246.38" y2="-180.34" width="0.1524" layer="91"/>
<wire x1="246.38" y1="-180.34" x2="256.54" y2="-190.5" width="0.1524" layer="91"/>
<pinref part="U$6" gate="G$1" pin="S"/>
<wire x1="256.54" y1="-190.5" x2="259.08" y2="-190.5" width="0.1524" layer="91"/>
</segment>
</net>
<net name="N$29" class="0">
<segment>
<pinref part="U$14" gate="G$1" pin="A"/>
<wire x1="337.82" y1="-48.26" x2="337.82" y2="-50.8" width="0.1524" layer="91"/>
<pinref part="U$4" gate="G$1" pin="D"/>
<wire x1="312.42" y1="-27.94" x2="322.58" y2="-27.94" width="0.1524" layer="91"/>
<wire x1="322.58" y1="-27.94" x2="322.58" y2="-50.8" width="0.1524" layer="91"/>
<wire x1="322.58" y1="-50.8" x2="337.82" y2="-50.8" width="0.1524" layer="91"/>
<wire x1="337.82" y1="-50.8" x2="342.9" y2="-50.8" width="0.1524" layer="91"/>
<wire x1="342.9" y1="-50.8" x2="353.06" y2="-40.64" width="0.1524" layer="91"/>
<pinref part="X5" gate="G$1" pin="2"/>
<wire x1="353.06" y1="-40.64" x2="358.14" y2="-40.64" width="0.1524" layer="91"/>
</segment>
</net>
<net name="N$30" class="0">
<segment>
<pinref part="R14" gate="G$1" pin="2"/>
<pinref part="U$5" gate="G$1" pin="A"/>
<wire x1="243.84" y1="-63.5" x2="254" y2="-63.5" width="0.1524" layer="91"/>
</segment>
</net>
<net name="N$37" class="0">
<segment>
<pinref part="R13" gate="G$1" pin="2"/>
<pinref part="U$4" gate="G$1" pin="G"/>
<wire x1="243.84" y1="-40.64" x2="284.48" y2="-40.64" width="0.1524" layer="91"/>
<pinref part="R15" gate="G$1" pin="2"/>
<wire x1="284.48" y1="-40.64" x2="299.72" y2="-40.64" width="0.1524" layer="91"/>
<wire x1="284.48" y1="-43.18" x2="284.48" y2="-40.64" width="0.1524" layer="91"/>
<junction x="284.48" y="-40.64"/>
</segment>
</net>
<net name="N$38" class="0">
<segment>
<pinref part="R14" gate="G$1" pin="1"/>
<wire x1="226.06" y1="-63.5" x2="215.9" y2="-63.5" width="0.1524" layer="91"/>
<pinref part="R13" gate="G$1" pin="1"/>
<wire x1="226.06" y1="-40.64" x2="215.9" y2="-40.64" width="0.1524" layer="91"/>
<wire x1="215.9" y1="-63.5" x2="215.9" y2="-40.64" width="0.1524" layer="91"/>
<wire x1="215.9" y1="-40.64" x2="177.8" y2="-40.64" width="0.1524" layer="91"/>
<junction x="215.9" y="-40.64"/>
<pinref part="U$1" gate="G$1" pin="D6"/>
<wire x1="58.42" y1="60.96" x2="165.1" y2="60.96" width="0.1524" layer="91"/>
<wire x1="165.1" y1="60.96" x2="167.64" y2="58.42" width="0.1524" layer="91"/>
<wire x1="167.64" y1="58.42" x2="167.64" y2="-30.48" width="0.1524" layer="91"/>
<wire x1="167.64" y1="-30.48" x2="177.8" y2="-40.64" width="0.1524" layer="91"/>
</segment>
</net>
<net name="N$39" class="0">
<segment>
<pinref part="U$21" gate="G$1" pin="A"/>
<wire x1="337.82" y1="-93.98" x2="337.82" y2="-96.52" width="0.1524" layer="91"/>
<wire x1="322.58" y1="-73.66" x2="322.58" y2="-96.52" width="0.1524" layer="91"/>
<wire x1="322.58" y1="-96.52" x2="337.82" y2="-96.52" width="0.1524" layer="91"/>
<wire x1="337.82" y1="-96.52" x2="342.9" y2="-96.52" width="0.1524" layer="91"/>
<wire x1="342.9" y1="-96.52" x2="353.06" y2="-86.36" width="0.1524" layer="91"/>
<pinref part="U$16" gate="G$1" pin="D"/>
<wire x1="322.58" y1="-73.66" x2="312.42" y2="-73.66" width="0.1524" layer="91"/>
<pinref part="X6" gate="G$1" pin="2"/>
<wire x1="353.06" y1="-86.36" x2="358.14" y2="-86.36" width="0.1524" layer="91"/>
</segment>
</net>
<net name="N$40" class="0">
<segment>
<pinref part="R17" gate="G$1" pin="2"/>
<pinref part="U$17" gate="G$1" pin="A"/>
<wire x1="243.84" y1="-109.22" x2="254" y2="-109.22" width="0.1524" layer="91"/>
</segment>
</net>
<net name="N$41" class="0">
<segment>
<pinref part="R16" gate="G$1" pin="2"/>
<pinref part="U$16" gate="G$1" pin="G"/>
<wire x1="243.84" y1="-86.36" x2="284.48" y2="-86.36" width="0.1524" layer="91"/>
<pinref part="R18" gate="G$1" pin="2"/>
<wire x1="284.48" y1="-86.36" x2="299.72" y2="-86.36" width="0.1524" layer="91"/>
<wire x1="284.48" y1="-88.9" x2="284.48" y2="-86.36" width="0.1524" layer="91"/>
<junction x="284.48" y="-86.36"/>
</segment>
</net>
<net name="N$42" class="0">
<segment>
<pinref part="R17" gate="G$1" pin="1"/>
<wire x1="226.06" y1="-109.22" x2="215.9" y2="-109.22" width="0.1524" layer="91"/>
<pinref part="R16" gate="G$1" pin="1"/>
<wire x1="226.06" y1="-86.36" x2="215.9" y2="-86.36" width="0.1524" layer="91"/>
<wire x1="215.9" y1="-109.22" x2="215.9" y2="-86.36" width="0.1524" layer="91"/>
<wire x1="215.9" y1="-86.36" x2="177.8" y2="-86.36" width="0.1524" layer="91"/>
<junction x="215.9" y="-86.36"/>
<pinref part="U$1" gate="G$1" pin="D7"/>
<wire x1="58.42" y1="55.88" x2="157.48" y2="55.88" width="0.1524" layer="91"/>
<wire x1="157.48" y1="55.88" x2="162.56" y2="50.8" width="0.1524" layer="91"/>
<wire x1="162.56" y1="50.8" x2="162.56" y2="-71.12" width="0.1524" layer="91"/>
<wire x1="162.56" y1="-71.12" x2="177.8" y2="-86.36" width="0.1524" layer="91"/>
</segment>
</net>
<net name="12V2" class="0">
<segment>
<pinref part="U$1" gate="G$1" pin="VIN"/>
<wire x1="7.62" y1="106.68" x2="7.62" y2="167.64" width="0.1524" layer="91"/>
<wire x1="7.62" y1="167.64" x2="5.08" y2="170.18" width="0.1524" layer="91"/>
<pinref part="U$36" gate="G$1" pin="VIN"/>
<wire x1="5.08" y1="170.18" x2="-20.32" y2="170.18" width="0.1524" layer="91"/>
<wire x1="7.62" y1="167.64" x2="10.16" y2="170.18" width="0.1524" layer="91"/>
<pinref part="U$12" gate="G$1" pin="K"/>
<wire x1="10.16" y1="170.18" x2="330.2" y2="170.18" width="0.1524" layer="91"/>
<wire x1="330.2" y1="170.18" x2="337.82" y2="162.56" width="0.1524" layer="91"/>
<wire x1="337.82" y1="162.56" x2="330.2" y2="154.94" width="0.1524" layer="91"/>
<pinref part="U$13" gate="G$1" pin="K"/>
<wire x1="330.2" y1="154.94" x2="330.2" y2="121.92" width="0.1524" layer="91"/>
<wire x1="330.2" y1="121.92" x2="337.82" y2="114.3" width="0.1524" layer="91"/>
<wire x1="337.82" y1="114.3" x2="330.2" y2="106.68" width="0.1524" layer="91"/>
<pinref part="U$24" gate="G$1" pin="K"/>
<wire x1="330.2" y1="106.68" x2="330.2" y2="73.66" width="0.1524" layer="91"/>
<wire x1="330.2" y1="73.66" x2="337.82" y2="66.04" width="0.1524" layer="91"/>
<wire x1="337.82" y1="66.04" x2="330.2" y2="58.42" width="0.1524" layer="91"/>
<pinref part="U$32" gate="G$1" pin="K"/>
<wire x1="330.2" y1="58.42" x2="330.2" y2="25.4" width="0.1524" layer="91"/>
<wire x1="330.2" y1="25.4" x2="337.82" y2="17.78" width="0.1524" layer="91"/>
<wire x1="337.82" y1="162.56" x2="345.44" y2="162.56" width="0.1524" layer="91"/>
<wire x1="345.44" y1="162.56" x2="355.6" y2="152.4" width="0.1524" layer="91"/>
<wire x1="337.82" y1="114.3" x2="345.44" y2="114.3" width="0.1524" layer="91"/>
<wire x1="345.44" y1="114.3" x2="355.6" y2="104.14" width="0.1524" layer="91"/>
<wire x1="337.82" y1="66.04" x2="345.44" y2="66.04" width="0.1524" layer="91"/>
<wire x1="345.44" y1="66.04" x2="355.6" y2="55.88" width="0.1524" layer="91"/>
<wire x1="337.82" y1="17.78" x2="345.44" y2="17.78" width="0.1524" layer="91"/>
<wire x1="345.44" y1="17.78" x2="355.6" y2="7.62" width="0.1524" layer="91"/>
<wire x1="337.82" y1="17.78" x2="330.2" y2="10.16" width="0.1524" layer="91"/>
<wire x1="330.2" y1="10.16" x2="330.2" y2="-20.32" width="0.1524" layer="91"/>
<pinref part="U$14" gate="G$1" pin="K"/>
<wire x1="337.82" y1="-27.94" x2="345.44" y2="-27.94" width="0.1524" layer="91"/>
<wire x1="345.44" y1="-27.94" x2="355.6" y2="-38.1" width="0.1524" layer="91"/>
<wire x1="330.2" y1="-20.32" x2="337.82" y2="-27.94" width="0.1524" layer="91"/>
<pinref part="U$21" gate="G$1" pin="K"/>
<wire x1="345.44" y1="-73.66" x2="355.6" y2="-83.82" width="0.1524" layer="91"/>
<wire x1="345.44" y1="-73.66" x2="337.82" y2="-73.66" width="0.1524" layer="91"/>
<wire x1="337.82" y1="-73.66" x2="332.74" y2="-68.58" width="0.1524" layer="91"/>
<wire x1="332.74" y1="-68.58" x2="332.74" y2="-33.02" width="0.1524" layer="91"/>
<wire x1="332.74" y1="-33.02" x2="337.82" y2="-27.94" width="0.1524" layer="91"/>
<pinref part="X6" gate="G$1" pin="1"/>
<wire x1="355.6" y1="-83.82" x2="358.14" y2="-83.82" width="0.1524" layer="91"/>
<pinref part="X5" gate="G$1" pin="1"/>
<wire x1="355.6" y1="-38.1" x2="358.14" y2="-38.1" width="0.1524" layer="91"/>
<pinref part="X4" gate="G$1" pin="1"/>
<wire x1="355.6" y1="7.62" x2="358.14" y2="7.62" width="0.1524" layer="91"/>
<pinref part="X3" gate="G$1" pin="1"/>
<wire x1="355.6" y1="55.88" x2="358.14" y2="55.88" width="0.1524" layer="91"/>
<pinref part="X2" gate="G$1" pin="1"/>
<wire x1="355.6" y1="104.14" x2="358.14" y2="104.14" width="0.1524" layer="91"/>
<pinref part="X1" gate="G$1" pin="1"/>
<wire x1="355.6" y1="152.4" x2="358.14" y2="152.4" width="0.1524" layer="91"/>
</segment>
</net>
<net name="GND2" class="0">
<segment>
<pinref part="U$16" gate="G$1" pin="S"/>
<wire x1="312.42" y1="-109.22" x2="312.42" y2="-91.44" width="0.1524" layer="91"/>
<pinref part="U$17" gate="G$1" pin="K"/>
<wire x1="274.32" y1="-109.22" x2="284.48" y2="-109.22" width="0.1524" layer="91"/>
<wire x1="284.48" y1="-109.22" x2="312.42" y2="-109.22" width="0.1524" layer="91"/>
<pinref part="R18" gate="G$1" pin="1"/>
<wire x1="284.48" y1="-106.68" x2="284.48" y2="-109.22" width="0.1524" layer="91"/>
<junction x="284.48" y="-109.22"/>
<wire x1="302.26" y1="-119.38" x2="312.42" y2="-109.22" width="0.1524" layer="91"/>
<pinref part="U$4" gate="G$1" pin="S"/>
<wire x1="312.42" y1="-63.5" x2="312.42" y2="-45.72" width="0.1524" layer="91"/>
<pinref part="U$5" gate="G$1" pin="K"/>
<wire x1="274.32" y1="-63.5" x2="284.48" y2="-63.5" width="0.1524" layer="91"/>
<wire x1="284.48" y1="-63.5" x2="312.42" y2="-63.5" width="0.1524" layer="91"/>
<pinref part="R15" gate="G$1" pin="1"/>
<wire x1="284.48" y1="-60.96" x2="284.48" y2="-63.5" width="0.1524" layer="91"/>
<junction x="284.48" y="-63.5"/>
<wire x1="302.26" y1="-73.66" x2="312.42" y2="-63.5" width="0.1524" layer="91"/>
<pinref part="U$20" gate="G$1" pin="S"/>
<pinref part="U$23" gate="G$1" pin="K"/>
<wire x1="274.32" y1="30.48" x2="284.48" y2="30.48" width="0.1524" layer="91"/>
<pinref part="R9" gate="G$1" pin="1"/>
<wire x1="284.48" y1="33.02" x2="284.48" y2="30.48" width="0.1524" layer="91"/>
<junction x="284.48" y="30.48"/>
<wire x1="284.48" y1="30.48" x2="312.42" y2="30.48" width="0.1524" layer="91"/>
<wire x1="312.42" y1="30.48" x2="312.42" y2="48.26" width="0.1524" layer="91"/>
<wire x1="312.42" y1="30.48" x2="304.8" y2="22.86" width="0.1524" layer="91"/>
<pinref part="U$3" gate="G$1" pin="S"/>
<wire x1="312.42" y1="78.74" x2="312.42" y2="96.52" width="0.1524" layer="91"/>
<pinref part="U$10" gate="G$1" pin="K"/>
<wire x1="274.32" y1="78.74" x2="284.48" y2="78.74" width="0.1524" layer="91"/>
<wire x1="284.48" y1="78.74" x2="312.42" y2="78.74" width="0.1524" layer="91"/>
<pinref part="R6" gate="G$1" pin="1"/>
<wire x1="284.48" y1="81.28" x2="284.48" y2="78.74" width="0.1524" layer="91"/>
<junction x="284.48" y="78.74"/>
<wire x1="312.42" y1="78.74" x2="304.8" y2="71.12" width="0.1524" layer="91"/>
<pinref part="U$2" gate="G$1" pin="S"/>
<wire x1="312.42" y1="127" x2="312.42" y2="144.78" width="0.1524" layer="91"/>
<pinref part="U$11" gate="G$1" pin="K"/>
<wire x1="274.32" y1="127" x2="284.48" y2="127" width="0.1524" layer="91"/>
<wire x1="284.48" y1="127" x2="312.42" y2="127" width="0.1524" layer="91"/>
<pinref part="R3" gate="G$1" pin="1"/>
<wire x1="284.48" y1="129.54" x2="284.48" y2="127" width="0.1524" layer="91"/>
<junction x="284.48" y="127"/>
<wire x1="312.42" y1="127" x2="304.8" y2="119.38" width="0.1524" layer="91"/>
<wire x1="304.8" y1="119.38" x2="203.2" y2="119.38" width="0.1524" layer="91"/>
<wire x1="203.2" y1="119.38" x2="198.12" y2="114.3" width="0.1524" layer="91"/>
<pinref part="U$1" gate="G$1" pin="GND"/>
<wire x1="22.86" y1="-15.24" x2="22.86" y2="-20.32" width="0.1524" layer="91"/>
<pinref part="U$36" gate="G$1" pin="GND"/>
<wire x1="-38.1" y1="170.18" x2="-45.72" y2="170.18" width="0.1524" layer="91"/>
<wire x1="-45.72" y1="170.18" x2="-48.26" y2="167.64" width="0.1524" layer="91"/>
<wire x1="-48.26" y1="167.64" x2="-48.26" y2="-22.86" width="0.1524" layer="91"/>
<wire x1="-48.26" y1="-22.86" x2="-43.18" y2="-27.94" width="0.1524" layer="91"/>
<wire x1="-43.18" y1="-27.94" x2="15.24" y2="-27.94" width="0.1524" layer="91"/>
<wire x1="15.24" y1="-27.94" x2="22.86" y2="-20.32" width="0.1524" layer="91"/>
<wire x1="22.86" y1="-20.32" x2="30.48" y2="-27.94" width="0.1524" layer="91"/>
<wire x1="30.48" y1="-27.94" x2="68.58" y2="-27.94" width="0.1524" layer="91"/>
<wire x1="68.58" y1="-27.94" x2="190.5" y2="-27.94" width="0.1524" layer="91"/>
<wire x1="198.12" y1="114.3" x2="198.12" y2="63.5" width="0.1524" layer="91"/>
<wire x1="198.12" y1="63.5" x2="198.12" y2="15.24" width="0.1524" layer="91"/>
<wire x1="198.12" y1="15.24" x2="198.12" y2="-20.32" width="0.1524" layer="91"/>
<wire x1="198.12" y1="-20.32" x2="190.5" y2="-27.94" width="0.1524" layer="91"/>
<wire x1="304.8" y1="71.12" x2="205.74" y2="71.12" width="0.1524" layer="91"/>
<wire x1="205.74" y1="71.12" x2="198.12" y2="63.5" width="0.1524" layer="91"/>
<wire x1="304.8" y1="22.86" x2="205.74" y2="22.86" width="0.1524" layer="91"/>
<wire x1="205.74" y1="22.86" x2="198.12" y2="15.24" width="0.1524" layer="91"/>
<pinref part="U$22" gate="G$1" pin="K"/>
<wire x1="68.58" y1="-22.86" x2="68.58" y2="-27.94" width="0.1524" layer="91"/>
<wire x1="198.12" y1="-20.32" x2="198.12" y2="-35.56" width="0.1524" layer="91"/>
<wire x1="198.12" y1="-35.56" x2="198.12" y2="-78.74" width="0.1524" layer="91"/>
<wire x1="198.12" y1="-78.74" x2="198.12" y2="-127" width="0.1524" layer="91"/>
<wire x1="198.12" y1="-127" x2="198.12" y2="-132.08" width="0.1524" layer="91"/>
<wire x1="198.12" y1="-132.08" x2="190.5" y2="-139.7" width="0.1524" layer="91"/>
<pinref part="U$39" gate="G$1" pin="2"/>
<wire x1="76.2" y1="-139.7" x2="86.36" y2="-139.7" width="0.1524" layer="91"/>
<pinref part="U$38" gate="G$1" pin="2"/>
<junction x="86.36" y="-139.7"/>
<wire x1="86.36" y1="-139.7" x2="96.52" y2="-139.7" width="0.1524" layer="91"/>
<pinref part="U$37" gate="G$1" pin="2"/>
<junction x="96.52" y="-139.7"/>
<wire x1="96.52" y1="-139.7" x2="106.68" y2="-139.7" width="0.1524" layer="91"/>
<pinref part="U$30" gate="G$1" pin="2"/>
<wire x1="190.5" y1="-139.7" x2="106.68" y2="-139.7" width="0.1524" layer="91"/>
<wire x1="302.26" y1="-73.66" x2="203.2" y2="-73.66" width="0.1524" layer="91"/>
<wire x1="203.2" y1="-73.66" x2="198.12" y2="-78.74" width="0.1524" layer="91"/>
<wire x1="302.26" y1="-119.38" x2="205.74" y2="-119.38" width="0.1524" layer="91"/>
<wire x1="205.74" y1="-119.38" x2="198.12" y2="-127" width="0.1524" layer="91"/>
<pinref part="U$28" gate="G$1" pin="S"/>
<wire x1="312.42" y1="-17.78" x2="312.42" y2="0" width="0.1524" layer="91"/>
<wire x1="302.26" y1="-27.94" x2="312.42" y2="-17.78" width="0.1524" layer="91"/>
<wire x1="284.48" y1="-17.78" x2="312.42" y2="-17.78" width="0.1524" layer="91"/>
<pinref part="R12" gate="G$1" pin="1"/>
<wire x1="284.48" y1="-15.24" x2="284.48" y2="-17.78" width="0.1524" layer="91"/>
<junction x="284.48" y="-17.78"/>
<pinref part="U$31" gate="G$1" pin="K"/>
<wire x1="274.32" y1="-17.78" x2="284.48" y2="-17.78" width="0.1524" layer="91"/>
<wire x1="302.26" y1="-27.94" x2="205.74" y2="-27.94" width="0.1524" layer="91"/>
<wire x1="205.74" y1="-27.94" x2="198.12" y2="-35.56" width="0.1524" layer="91"/>
</segment>
</net>
<net name="N$14" class="0">
<segment>
<pinref part="U$8" gate="G$1" pin="5"/>
<wire x1="205.74" y1="-190.5" x2="101.6" y2="-190.5" width="0.1524" layer="91"/>
<wire x1="101.6" y1="-190.5" x2="86.36" y2="-175.26" width="0.1524" layer="91"/>
<pinref part="U$38" gate="G$1" pin="1"/>
<wire x1="86.36" y1="-175.26" x2="86.36" y2="-157.48" width="0.1524" layer="91"/>
</segment>
</net>
<net name="N$5" class="0">
<segment>
<pinref part="U$30" gate="G$1" pin="1"/>
<wire x1="106.68" y1="-157.48" x2="106.68" y2="-165.1" width="0.1524" layer="91"/>
<wire x1="106.68" y1="-165.1" x2="111.76" y2="-170.18" width="0.1524" layer="91"/>
<pinref part="U$8" gate="G$1" pin="1"/>
<wire x1="111.76" y1="-170.18" x2="205.74" y2="-170.18" width="0.1524" layer="91"/>
</segment>
</net>
<net name="N$8" class="0">
<segment>
<pinref part="U$37" gate="G$1" pin="1"/>
<wire x1="96.52" y1="-157.48" x2="96.52" y2="-170.18" width="0.1524" layer="91"/>
<wire x1="96.52" y1="-170.18" x2="106.68" y2="-180.34" width="0.1524" layer="91"/>
<pinref part="U$8" gate="G$1" pin="3"/>
<wire x1="106.68" y1="-180.34" x2="205.74" y2="-180.34" width="0.1524" layer="91"/>
</segment>
</net>
<net name="N$15" class="0">
<segment>
<pinref part="U$39" gate="G$1" pin="1"/>
<wire x1="76.2" y1="-157.48" x2="76.2" y2="-182.88" width="0.1524" layer="91"/>
<wire x1="76.2" y1="-182.88" x2="93.98" y2="-200.66" width="0.1524" layer="91"/>
<pinref part="U$8" gate="G$1" pin="7"/>
<wire x1="93.98" y1="-200.66" x2="205.74" y2="-200.66" width="0.1524" layer="91"/>
</segment>
</net>
<net name="N$20" class="0">
<segment>
<pinref part="U$1" gate="G$1" pin="D8"/>
<wire x1="58.42" y1="50.8" x2="152.4" y2="50.8" width="0.1524" layer="91"/>
<wire x1="152.4" y1="50.8" x2="157.48" y2="45.72" width="0.1524" layer="91"/>
<wire x1="157.48" y1="45.72" x2="157.48" y2="-170.18" width="0.1524" layer="91"/>
<wire x1="157.48" y1="-170.18" x2="162.56" y2="-175.26" width="0.1524" layer="91"/>
<pinref part="U$8" gate="G$1" pin="2"/>
<wire x1="162.56" y1="-175.26" x2="205.74" y2="-175.26" width="0.1524" layer="91"/>
</segment>
</net>
<net name="N$21" class="0">
<segment>
<pinref part="U$1" gate="G$1" pin="D9"/>
<wire x1="58.42" y1="45.72" x2="147.32" y2="45.72" width="0.1524" layer="91"/>
<wire x1="147.32" y1="45.72" x2="152.4" y2="40.64" width="0.1524" layer="91"/>
<wire x1="152.4" y1="40.64" x2="152.4" y2="-180.34" width="0.1524" layer="91"/>
<wire x1="152.4" y1="-180.34" x2="157.48" y2="-185.42" width="0.1524" layer="91"/>
<pinref part="U$8" gate="G$1" pin="4"/>
<wire x1="157.48" y1="-185.42" x2="205.74" y2="-185.42" width="0.1524" layer="91"/>
</segment>
</net>
<net name="N$25" class="0">
<segment>
<pinref part="U$1" gate="G$1" pin="D10"/>
<wire x1="58.42" y1="40.64" x2="142.24" y2="40.64" width="0.1524" layer="91"/>
<wire x1="142.24" y1="40.64" x2="147.32" y2="35.56" width="0.1524" layer="91"/>
<wire x1="147.32" y1="35.56" x2="147.32" y2="-185.42" width="0.1524" layer="91"/>
<wire x1="147.32" y1="-185.42" x2="157.48" y2="-195.58" width="0.1524" layer="91"/>
<pinref part="U$8" gate="G$1" pin="6"/>
<wire x1="157.48" y1="-195.58" x2="205.74" y2="-195.58" width="0.1524" layer="91"/>
</segment>
</net>
<net name="N$26" class="0">
<segment>
<pinref part="U$1" gate="G$1" pin="D11"/>
<wire x1="58.42" y1="35.56" x2="134.62" y2="35.56" width="0.1524" layer="91"/>
<wire x1="134.62" y1="35.56" x2="139.7" y2="30.48" width="0.1524" layer="91"/>
<wire x1="139.7" y1="30.48" x2="139.7" y2="-193.04" width="0.1524" layer="91"/>
<wire x1="139.7" y1="-193.04" x2="152.4" y2="-205.74" width="0.1524" layer="91"/>
<pinref part="U$8" gate="G$1" pin="8"/>
<wire x1="152.4" y1="-205.74" x2="205.74" y2="-205.74" width="0.1524" layer="91"/>
</segment>
</net>
</nets>
</sheet>
</sheets>
<errors>
<approved hash="108,1,271.78,139.7,N$2,,,,,"/>
<approved hash="108,1,271.78,91.44,N$7,,,,,"/>
<approved hash="108,1,271.78,43.18,N$13,,,,,"/>
<approved hash="108,1,271.78,-5.08,N$19,,,,,"/>
<approved hash="108,1,271.78,17.78,N$4,,,,,"/>
<approved hash="108,1,271.78,114.3,N$4,,,,,"/>
<approved hash="108,1,271.78,66.04,N$4,,,,,"/>
<approved hash="108,1,7.62,167.64,N$4,,,,,"/>
<approved hash="108,1,271.78,162.56,N$4,,,,,"/>
<approved hash="108,1,246.38,127,N$9,,,,,"/>
<approved hash="108,1,246.38,-17.78,N$9,,,,,"/>
<approved hash="108,1,124.46,-27.94,N$9,,,,,"/>
<approved hash="108,1,68.58,-27.94,N$9,,,,,"/>
<approved hash="108,1,132.08,63.5,N$9,,,,,"/>
<approved hash="108,1,246.38,30.48,N$9,,,,,"/>
<approved hash="108,1,22.86,-20.32,N$9,,,,,"/>
<approved hash="108,1,246.38,78.74,N$9,,,,,"/>
<approved hash="108,1,132.08,15.24,N$9,,,,,"/>
<approved hash="110,1,170.18,-238.76,N$20,N$8,,,,"/>
<approved hash="110,1,170.18,-238.76,N$20,N$8,,,,"/>
<approved hash="110,1,160.02,-248.92,N$21,N$14,,,,"/>
<approved hash="110,1,160.02,-248.92,N$21,N$14,,,,"/>
</errors>
</schematic>
</drawing>
</eagle>
