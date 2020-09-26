package cn.tedu.store.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.tedu.store.bean.Area;
import cn.tedu.store.bean.City;
import cn.tedu.store.bean.Province;
import cn.tedu.store.mapper.DistrictMapper;

@Service("districtService")
public class DistrictServiceImpl 
	implements IDistrictService {
	
	@Resource(name="districtMapper")
	private DistrictMapper districtMapper;

	public List<Province> getProvinces() {
		return districtMapper.getProvinces();
	}

	public List<City> getCities(String provinceCode) {
		return districtMapper.getCities(provinceCode);
	}

	public List<Area> getAreas(String cityCode) {
		return districtMapper.getAreas(cityCode);
	}

	public String getProvinceNameByCode(String provinceCode) {
		return districtMapper.getProvinceNameByCode(provinceCode);
	}

	public String getCityNameByCode(String cityCode) {
		return districtMapper.getCityNameByCode(cityCode);
	}

	public String getAreaNameByCode(String areaCode) {
		return districtMapper.getAreaNameByCode(areaCode);
	}

}
