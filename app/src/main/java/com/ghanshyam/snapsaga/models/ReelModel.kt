package com.ghanshyam.snapsaga.models

class ReelModel {

    var reelUrl: String = ""
    var caption: String = ""

    constructor()
    constructor(reelUrl: String, caption: String) {
        this.reelUrl = reelUrl
        this.caption = caption
    }


}