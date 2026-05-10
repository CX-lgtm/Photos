---
title: "风暴光影"
date: 2026-05-08T22:21:00+09:00
description: "一组用于验证发布流程的示例相册。你可以把这里替换成自己的旅拍、街拍、人像或商业作品。"
categories: ["作品集", "调色"]
keywords: ["摄影", "作品集", "Hugo", "光影"]
menus:
  main:
    name: "样片"
    weight: 10
params:
  sort_by: "Params.weight"
  sort_order: "asc"
  theme: "dark"
resources:
  - src: feature.jpg
    title: "风暴光影 01"
    params:
      cover: true
      weight: 10
  - src: storm-light-02.jpg
    title: "风暴光影 02"
    params:
      weight: 20
---

这页既是相册，也是博客文章。你可以写拍摄当天的天气、光线判断、机位选择、后期思路，下面的图片会自动进入灯箱画廊。

发布到 GitHub Pages 时，站点会只包含 Hugo 处理后的网页图片；原片、RAW 和高分辨率素材建议放在 `photos_raw/`，默认不会提交到 Git。
