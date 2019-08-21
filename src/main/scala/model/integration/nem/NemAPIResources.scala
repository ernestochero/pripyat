package model.integration.nem

object NemAPIResources {
  def nemHeartBeat() = s"/heartbeat"
  def nemStatus() = s"/status"
  def nemGenerateAccount() = s"/account/generate"
}
