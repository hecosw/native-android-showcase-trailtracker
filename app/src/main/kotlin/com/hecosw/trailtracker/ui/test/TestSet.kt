package com.hecosw.trailtracker.ui.test

import com.hecosw.trailtracker.domain.entity.Photo
import com.hecosw.trailtracker.domain.value.PhotoId
import com.hecosw.trailtracker.domain.value.PhotoTitle
import com.hecosw.trailtracker.domain.value.PhotoUrl
import okhttp3.internal.toImmutableList

object TestSet {
    val set01 = listOf(
            Photo(PhotoId(111111), PhotoTitle("Foo"), PhotoUrl("https://wallpapercave.com/wp/wp2782413.jpg")),
            Photo(PhotoId(222222), PhotoTitle("Foo"), PhotoUrl("https://www.pixelstalk.net/wp-content/uploads/2016/06/Sea-Waterfall-Wallpaper-High-Quality.jpg")),
            Photo(PhotoId(555555), PhotoTitle("Foo"), PhotoUrl("https://wallpaperaccess.com/full/251722.jpg")),
            Photo(PhotoId(666666), PhotoTitle("Foo"), PhotoUrl("https://eskipaper.com/images/high-resolution-photos-1.jpg")),
            Photo(PhotoId(777777), PhotoTitle("Foo"), PhotoUrl("https://vastphotos.com/files/uploads/photos/10252/mountains-in-autumn-landscape-l.jpg")),
            Photo(PhotoId(333333), PhotoTitle("Foo"), PhotoUrl("https://wallpaperaccess.com/full/2416436.jpg")),
            Photo(PhotoId(444444), PhotoTitle("Foo"), PhotoUrl("https://bordalo.observador.pt/v2/q:84/c:2000:1124:nowe:0:104/rs:fill:1280/f:webp/plain/https://s3.observador.pt/wp-content/uploads/2024/01/17134235/42163746.jpg")),
            Photo(PhotoId(1), PhotoTitle("Foo"), PhotoUrl("https://live.staticflickr.com/4465/36755072274_5496c3cb35_m.jpg")),
            Photo(PhotoId(2), PhotoTitle("Foo"), PhotoUrl("https://live.staticflickr.com/65535/49996770481_6a407e9255_m.jpg")),
            Photo(PhotoId(4), PhotoTitle("Foo"), PhotoUrl("https://live.staticflickr.com/6171/6202050551_455711b823_m.jpg")),
            Photo(PhotoId(5), PhotoTitle("Foo"), PhotoUrl("https://live.staticflickr.com/3901/15008700875_0e262eee71_m.jpg")),
            Photo(PhotoId(10), PhotoTitle("Foo"), PhotoUrl("https://live.staticflickr.com/4465/36755072274_5496c3cb35_m.jpg")),
            Photo(PhotoId(20), PhotoTitle("Foo"), PhotoUrl("https://live.staticflickr.com/65535/49996770481_6a407e9255_m.jpg")),
            Photo(PhotoId(40), PhotoTitle("Foo"), PhotoUrl("https://live.staticflickr.com/6171/6202050551_455711b823_m.jpg")),
            Photo(PhotoId(50), PhotoTitle("Foo"), PhotoUrl("https://live.staticflickr.com/3901/15008700875_0e262eee71_m.jpg")),
            Photo(PhotoId(11), PhotoTitle("Foo"), PhotoUrl("https://live.staticflickr.com/4465/36755072274_5496c3cb35_m.jpg")),
            Photo(PhotoId(21), PhotoTitle("Foo"), PhotoUrl("https://live.staticflickr.com/65535/49996770481_6a407e9255_m.jpg")),
            Photo(PhotoId(41), PhotoTitle("Foo"), PhotoUrl("https://live.staticflickr.com/6171/6202050551_455711b823_m.jpg")),
            Photo(PhotoId(51), PhotoTitle("Foo"), PhotoUrl("https://live.staticflickr.com/3901/15008700875_0e262eee71_m.jpg")),
            Photo(PhotoId(13), PhotoTitle("Foo"), PhotoUrl("https://live.staticflickr.com/4465/36755072274_5496c3cb35_m.jpg")),
            Photo(PhotoId(23), PhotoTitle("Foo"), PhotoUrl("https://live.staticflickr.com/65535/49996770481_6a407e9255_m.jpg")),
            Photo(PhotoId(43), PhotoTitle("Foo"), PhotoUrl("https://live.staticflickr.com/6171/6202050551_455711b823_m.jpg")),
            Photo(PhotoId(53), PhotoTitle("Foo"), PhotoUrl("https://live.staticflickr.com/3901/15008700875_0e262eee71_m.jpg")),
            Photo(PhotoId(14), PhotoTitle("Foo"), PhotoUrl("https://live.staticflickr.com/4465/36755072274_5496c3cb35_m.jpg")),
            Photo(PhotoId(24), PhotoTitle("Foo"), PhotoUrl("https://live.staticflickr.com/65535/49996770481_6a407e9255_m.jpg")),
            Photo(PhotoId(44), PhotoTitle("Foo"), PhotoUrl("https://live.staticflickr.com/6171/6202050551_455711b823_m.jpg")),
            Photo(PhotoId(54), PhotoTitle("Foo"), PhotoUrl("https://live.staticflickr.com/3901/15008700875_0e262eee71_m.jpg")),
            Photo(PhotoId(15), PhotoTitle("Foo"), PhotoUrl("https://live.staticflickr.com/4465/36755072274_5496c3cb35_m.jpg")),
            Photo(PhotoId(25), PhotoTitle("Foo"), PhotoUrl("https://live.staticflickr.com/65535/49996770481_6a407e9255_m.jpg")),
            Photo(PhotoId(45), PhotoTitle("Foo"), PhotoUrl("https://live.staticflickr.com/6171/6202050551_455711b823_m.jpg")),
            Photo(PhotoId(55), PhotoTitle("Foo"), PhotoUrl("https://live.staticflickr.com/3901/15008700875_0e262eee71_m.jpg")),
    ).toImmutableList()
}
