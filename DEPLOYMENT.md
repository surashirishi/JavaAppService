# Azure App Service 設定ガイド

このプロジェクトは GitHub Actions を使用して Azure App Service に自動デプロイされます。

## 必要な設定

### 1. Azure App Service の作成

1. Azure Portal にログインします
2. 新しい App Service を作成します
3. 以下の設定を使用してください：
   - **Runtime**: Java 17
   - **Operating System**: Windows (web.config ファイルを使用)
   - **Plan**: 適切なプランを選択

### 2. GitHub Secrets の設定

リポジトリの Settings → Secrets and variables → Actions で以下のシークレットを追加してください：

- `AZURE_WEBAPP_NAME`: Azure App Service の名前
- `AZURE_WEBAPP_PUBLISH_PROFILE`: パブリッシュプロファイルの内容

### 3. パブリッシュプロファイルの取得

1. Azure Portal で App Service を開きます
2. 「概要」ページで「パブリッシュプロファイルのダウンロード」をクリック
3. ダウンロードした XML ファイルの内容全体をコピー
4. GitHub の `AZURE_WEBAPP_PUBLISH_PROFILE` シークレットとして貼り付け

## デプロイメントの流れ

1. **トリガー**: main ブランチへのプッシュ
2. **ビルド**: Maven を使用してアプリケーションをビルド
3. **テスト**: 全てのテストを実行
4. **デプロイ**: Azure App Service にデプロイ

## 確認事項

- Java 17 がインストールされていること
- Maven が正常に動作すること
- 全てのテストが成功すること
- Azure App Service が Java 17 に設定されていること